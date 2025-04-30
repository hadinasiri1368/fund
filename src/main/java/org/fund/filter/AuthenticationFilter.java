package org.fund.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.fund.authentication.permission.PermissionService;
import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.config.authentication.AuthenticationTokenServiceImpl;
import org.fund.config.authentication.TokenService;
import org.fund.config.dataBase.TenantContext;
import org.fund.config.dataBase.TenantDataSourceManager;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.fund.dto.ExceptionDto;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Permission;
import org.fund.model.Users;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final TenantDataSourceManager tenantService;
    private final PermissionService permissionService;

    public AuthenticationFilter(TokenService tokenService
            , TenantDataSourceManager tenantService
            , PermissionService permissionService) {
        this.tokenService = tokenService;
        this.tenantService = tenantService;
        this.permissionService = permissionService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getHeader(Consts.HEADER_TENANT_PARAM_NAME) == null) {
                throw new FundException(GeneralExceptionType.SCHEMAID_ID_IS_NULL);
            }
            setRequest(request);
            String token = FundUtils.getToken(request);
            if (FundUtils.isNull(token))
                throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
            if (FundUtils.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                Users user = tokenService.getTokenData(RequestContext.getTokenId(), token);
                permissionService.validateUserAccess(user, request.getRequestURI());
                UserDetails userDetails = getUserDetails(user);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
                checkTenant(request);
                printLog(request, request.getAttribute(Consts.HEADER_UUID_PARAM_NAME).toString(), TenantContext.getCurrentTenant());
                filterChain.doFilter(request, response);
            }
        } catch (FundException e) {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                    HttpStatus.UNAUTHORIZED, e.getMessage(), currentTime, null);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().write(convertObjectToJson(ExceptionDto.builder()
                    .code(e.getMessage())
                    .message(FundUtils.getMessage(e.getMessage(), e.getParams()))
                    .uuid(null)
                    .time(currentTime)
                    .build()));
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        try {
            if (request.getMethod().equals("OPTIONS"))
                return true;
            if (permissionService.isBypassedUrl(request.getRequestURI()))
                return true;
            if (request.getHeader(Consts.HEADER_TENANT_PARAM_NAME) == null)
                return false;
            checkTenant(request);
            return permissionService.isSensitiveUrl(request.getRequestURI());
        } catch (Exception e) {
            HttpStatus httpStatus = e instanceof FundException ? ((FundException) e).getStatus() : HttpStatus.INTERNAL_SERVER_ERROR;
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                    httpStatus, e.getMessage(), currentTime, null);
            return false;
        }
    }

    private UserDetails getUserDetails(Users user) {
        List<SimpleGrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority("admin"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getIsActive(), false, false, false, auths);
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private void checkTenant(HttpServletRequest request) {
        String tenantId = request.getHeader(Consts.HEADER_TENANT_PARAM_NAME);
        if (FundUtils.isNull(tenantId)) {
            throw new FundException(GeneralExceptionType.SCHEMAID_ID_IS_NULL);
        }
        if (FundUtils.isNull(tenantService.getTenantDataSource(tenantId))) {
            throw new FundException(GeneralExceptionType.SCHEMAID_ID_IS_NOT_VALID);
        }
        request.getHeader(Consts.HEADER_UUID_PARAM_NAME);
        TenantContext.setCurrentTenant(tenantId);
        String uuid = FundUtils.generateUUID().toString();
        request.setAttribute(Consts.HEADER_UUID_PARAM_NAME, uuid);
        RequestContext.setUuid(uuid);
    }

    private void printLog(HttpServletRequest request, String uuid, String tenantId) {
        String startTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
        log.info(String.format("RequestURL: %s | Start Date : %s | uuid : %s | schemaId : %s", request.getRequestURL(), startTime, uuid, tenantId));
        String endTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
        log.info(String.format("RequestURL: %s | Start Date : %s | End Date : %s | uuid : %s", request.getRequestURL(), startTime, endTime, uuid));
    }

    private void setRequest(HttpServletRequest request){
        String token = FundUtils.getToken(request);
        Users user = JwtUtil.getTokenData(token);
        RequestContext.setUser(user);
        RequestContext.setToken(token);
    }
}
