package org.fund.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.fund.constant.Consts;
import org.fund.config.dataBase.TenantDataSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class TenantFilter extends OncePerRequestFilter {
    private TenantDataSourceManager tenantService;

    private static final Logger log = LoggerFactory.getLogger(TenantFilter.class);

    public TenantFilter(TenantDataSourceManager tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenantId = request.getHeader(Consts.HEADER_TENANT_PARAM_NAME);
        if (FundUtils.isNull(tenantId)) {
            log.info("schemaId id is null");
            throw new ServletException("schemaId id is null");
        }
        if (FundUtils.isNull(tenantService.getTenantDataSource(tenantId))) {
            log.info(String.format("schemaId id %s not found", tenantId));
            throw new ServletException("tenant not found");
        }
        TenantContext.setCurrentTenant(tenantId);
        filterChain.doFilter(request, response);
    }
}