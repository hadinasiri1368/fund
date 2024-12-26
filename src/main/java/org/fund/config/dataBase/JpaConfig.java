package org.fund.config.dataBase;

import org.hibernate.cfg.Environment;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JpaConfig {

    private final DataSource dataSource;

    public JpaConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public org.hibernate.cfg.Configuration hibernateConfiguration(
            CustomMultiTenantConnectionProvider multiTenantConnectionProvider,
            CustomTenantIdentifierResolver tenantIdentifierResolver) {

        Properties properties = new Properties();
        properties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        properties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);

        org.hibernate.cfg.Configuration configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(properties);
        return configuration;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(dataSource)
                .packages("org.fund")
                .build();
        factory.getJpaPropertyMap().put(Environment.DIALECT, "org.hibernate.dialect.OracleDialect");
        return factory;
    }

    @Bean
    public CustomMultiTenantConnectionProvider multiTenantConnectionProvider() {
        return new CustomMultiTenantConnectionProvider(dataSource);
    }

    @Bean
    public CustomTenantIdentifierResolver tenantIdentifierResolver() {
        return new CustomTenantIdentifierResolver();
    }
}
