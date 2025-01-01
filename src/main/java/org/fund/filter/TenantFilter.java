package org.fund.filter;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.constant.Consts;
import org.fund.config.dataBase.TenantDataSourceManager;
import org.fund.constant.TimeFormat;
import org.fund.dto.ExceptionDto;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class TenantFilter extends OncePerRequestFilter {
    private TenantDataSourceManager tenantService;

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    public TenantFilter(TenantDataSourceManager tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(Consts.HEADER_TENANT_PARAM_NAME);
        try {
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
            String startTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            log.info(String.format("RequestURL: %s | Start Date : %s | uuid : %s", request.getRequestURL(), startTime, uuid));
            filterChain.doFilter(request, response);
            String endTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            log.info(String.format("RequestURL: %s | Start Date : %s | End Date : %s | uuid : %s", request.getRequestURL(), startTime, endTime, uuid));
        } catch (FundException e) {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));
            log.error("exception occurred: httpStatus={}, message={}, time={}, uuid={}",
                    e.getStatus(), e.getMessage(), currentTime, null);
            response.setStatus(e.getStatus().value());
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

    private String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}