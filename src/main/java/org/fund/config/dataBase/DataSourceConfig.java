package org.fund.config.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.fund.common.CommonUtils;
import org.fund.common.EncryptTools;
import org.fund.common.TimeUtils;
import org.fund.constant.Consts;
import org.fund.constant.TimeFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    @Primary
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password.startsWith("ENC(") ? EncryptTools.decrypt(password.substring(4, password.length() - 1), EncryptTools.SECRET) : password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

}
