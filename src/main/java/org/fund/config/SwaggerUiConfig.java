package org.fund.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.fund.constant.Consts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerUiConfig implements WebMvcConfigurer {
    @Value("${springdoc.swagger-ui.server-url}")
    private String serverUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url(serverUrl));

        return new OpenAPI()
                .servers(servers)
                .info(new Info()
                        .title("Financial API")
                        .version("1.0")
                        .description("API documentation with global tenant header"))
                .addSecurityItem(new SecurityRequirement().addList(Consts.HEADER_TENANT_PARAM_NAME))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes(Consts.HEADER_TENANT_PARAM_NAME,
                                new SecurityScheme()
                                        .name(Consts.HEADER_TENANT_PARAM_NAME)
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
