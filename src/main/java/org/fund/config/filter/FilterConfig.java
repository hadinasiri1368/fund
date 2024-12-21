package org.fund.config.filter;

import org.apache.tomcat.util.bcel.Const;
import org.fund.constant.Consts;
import org.fund.filter.TenantFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<TenantFilter> loggingFilter() {
        FilterRegistrationBean<TenantFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantFilter());
        registrationBean.addUrlPatterns(Consts.PREFIX_API_URL + "*");
        return registrationBean;
    }
}

