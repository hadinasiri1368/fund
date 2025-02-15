package org.fund.config.request;

import org.fund.authentication.permission.PermissionService;
import org.fund.constant.Consts;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final PermissionService permissionService;

    public WebConfig(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor(permissionService))
                .addPathPatterns("/**");
    }
}
