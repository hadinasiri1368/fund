package org.fund.config.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.fund.common.CommonUtils;
import org.fund.common.FundUtils;
import org.fund.common.JwtUtil;
import org.fund.config.dataBase.TenantContext;
import org.fund.config.request.RequestContext;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.fund.dto.ExceptionDto;
import org.fund.exception.AuthenticationExceptionType;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.Fund;
import org.fund.model.Users;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Configuration
public class Logout implements LogoutHandler {
    private final TokenService tokenService;

    public Logout(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        try {
            String token = FundUtils.getToken(request);
            if (FundUtils.isNull(token))
                throw new FundException(AuthenticationExceptionType.TOKEN_IS_NULL);
            tokenService.removeTokenById(FundUtils.getCurrentTenant(request), JwtUtil.getTokenData(token).getId(), token);
        } catch (FundException e) {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            try {
                writeResponse(response, currentTime, e.getMessage(), e.getParams());
            } catch (Exception exception) {
                log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                        HttpStatus.UNAUTHORIZED, e.getMessage(), currentTime, null);
            }
        } catch (Exception exception) {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            try {
                writeResponse(response, currentTime, "unhandled_exception.error", null);
            } catch (Exception exception1) {
                log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                        HttpStatus.UNAUTHORIZED, exception1.getMessage(), currentTime, null);
            }
        }
    }

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    private void writeResponse(HttpServletResponse response, String currentTime, String message, Object[] params) throws Exception {
        log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                HttpStatus.UNAUTHORIZED, message, currentTime, null);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().write(convertObjectToJson(ExceptionDto.builder()
                .code(message)
                .message(FundUtils.getMessage(message, params))
                .uuid(null)
                .time(currentTime)
                .build()));
    }
}
