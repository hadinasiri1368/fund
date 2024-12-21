package org.fund.config.dataBase;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.fund.common.CommonUtils;
import org.fund.common.TimeUtils;
import org.fund.constant.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;



@Service
public class TenantService {

    private static final Logger log = LoggerFactory.getLogger(TenantService.class);
    @Value("${spring.datasource.minimum-idle}")
    private Integer minimumIdle;

    @Value("${spring.datasource.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.datasource.connection-timeout}")
    private Long connectionTimeout;

    @Value("${spring.datasource.idle-timeout}")
    private Long idleTimeout;

    @Value("${spring.datasource.initialization-fail-timeout}")
    private Long initializationFailTimeout;


    private Map<String, DataSource> tenantDataSources = new HashMap<>();

    private void initializeSchemas(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            // Example query to get schemas
            String query = CommonUtils.loadSqlFromFile(Consts.DEFAULT_FOLDER_ADDRESS + Consts.SCHEMAS_QUERY_FILE_NAME);
            var resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                log.info("there is no schema for connection");
            }
            // Logic to create connections for each schema
            int count = 0;
            while (resultSet.next()) {
                String schemaName = resultSet.getString("SCHEMA_NAME");
                log.info("try connection to schema : " + schemaName + "at " + TimeUtils.getNowTime());
                DataSource tenantDataSource = createTenantDataSource(schemaName
                        , resultSet.getString("DB_URL")
                        , resultSet.getString("AVDF_URL")
                        , resultSet.getString("SCHEMA_PASS"));
                if (!CommonUtils.isNull(tenantDataSource)) {
                    tenantDataSources.put(schemaName, tenantDataSource);
                    log.info(String.format("connection to schema {0} was established at {1}", schemaName, TimeUtils.getNowTime()));
                }
                count++;
            }
            log.info(String.format("connection established with {0} schemas", count));
        } catch (Exception e) {
            log.info("initializeSchemas has error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DataSource createTenantDataSource(String schemaName, String url, String avdfUrl, String password) {
        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
            String dburl = CommonUtils.isDebugMode() && !CommonUtils.isNull(avdfUrl) ? avdfUrl : url;
            dataSource.setJdbcUrl(Consts.JDBC_URL_PREFIX.concat(dburl));
            dataSource.setUsername(schemaName);
            dataSource.setPassword(password);
            dataSource.setPoolName(schemaName);
            dataSource.setMinimumIdle(minimumIdle);
            dataSource.setMaximumPoolSize(maximumPoolSize);
            dataSource.setConnectionTimeout(connectionTimeout);
            dataSource.setIdleTimeout(idleTimeout);
            dataSource.setInitializationFailTimeout(initializationFailTimeout);
            dataSource.setRegisterMbeans(true);
            if (executeQuery(dataSource, "select 1 from dual"))
                return dataSource;
        } catch (Exception e) {
            log.info("createTenantDataSource has error : " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public DataSource getTenantDataSource(String tenantId) {
        return tenantDataSources.get(tenantId);
    }

    private boolean executeQuery(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(query);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

