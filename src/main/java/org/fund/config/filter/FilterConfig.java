package org.fund.config.filter;

import org.fund.constant.Consts;
import org.fund.filter.TenantFilter;
import org.fund.config.dataBase.TenantDataSourceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Autowired
    private TenantDataSourceManager tenantService;
    @Bean
    public FilterRegistrationBean<TenantFilter> loggingFilter() {
        FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantFilter(tenantService));
        registrationBean.addUrlPatterns(Consts.PREFIX_API_URL + "*");
        return registrationBean;
    }
}

