package org.fund.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.fund.common.CommonUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.config.dataBase.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.sql.DataSource;
import java.io.IOException;

@Component
public class TenantFilter extends OncePerRequestFilter {
    @Autowired
    private TenantService tenantService;

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader("schemaId");
        if (CommonUtils.isNull(tenantId)) {
            log.info("schemaId id is null");
            throw new ServletException("schemaId id is null");
        }
        DataSource tenantDataSource = tenantService.getTenantDataSource(tenantId);
        if (CommonUtils.isNull(tenantDataSource)) {
            log.info(String.format("schemaId id {0} not found", tenantId));
            throw new ServletException("tenant not found");
        }
        TenantContext.setCurrentDataSource(tenantDataSource);
        filterChain.doFilter(request, response);
    }
}