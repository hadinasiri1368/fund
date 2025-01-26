package org.fund.config.request;

import org.fund.constant.Consts;
import org.fund.repository.JpaRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final JpaRepository repository;
    public WebConfig(final JpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestContextInterceptor(repository))
                .addPathPatterns("/**");
    }
}
