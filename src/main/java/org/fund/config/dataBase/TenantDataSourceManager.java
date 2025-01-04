package org.fund.config.dataBase;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import org.fund.common.FundUtils;
import org.fund.common.TimeUtils;
import org.fund.constant.Consts;
import org.fund.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Component
public class TenantDataSourceManager {
    private final ProfileService profileService;
    private final DataSource dataSource;

    public TenantDataSourceManager(ProfileService profileService, DataSource dataSource) {
        this.profileService = profileService;
        this.dataSource = dataSource;
    }

    @Value("${spring.tenant-datasource.minimum-idle}")
    private Integer minimumIdle;

    @Value("${spring.tenant-datasource.maximum-pool-size}")
    private Integer maximumPoolSize;

    @Value("${spring.tenant-datasource.connection-timeout}")
    private Long connectionTimeout;

    @Value("${spring.tenant-datasource.idle-timeout}")
    private Long idleTimeout;

    @Value("${spring.tenant-datasource.initialization-fail-timeout}")
    private Long initializationFailTimeout;

    private static final Logger log = LoggerFactory.getLogger(TenantDataSourceManager.class);

    private Map<String, DataSource> tenantDataSources = new HashMap<>();

    private void initializeSchemas(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            // Example query to get schemas
            String query = FundUtils.loadSqlFromFile(Consts.DEFAULT_FOLDER_ADDRESS + Consts.SCHEMAS_QUERY_FILE_NAME);
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
                        , resultSet.getString("SCHEMA_PASS"));
                if (!FundUtils.isNull(tenantDataSource)) {
                    tenantDataSources.put(Consts.DATASOURCE_MAP_KEY, tenantDataSource);
                    log.info(String.format("connection to schema %s was established at %s", schemaName, TimeUtils.getNowTime()));
                }
                count++;
            }
            log.info(String.format("connection established with %s schemas", count));
        } catch (Exception e) {
            log.info("initializeSchemas has error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeSchemas() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonInput = FundUtils.loadSqlFromFile(Consts.DEFAULT_FOLDER_ADDRESS + Consts.SCHEMAS_LIST_FILE_NAME);
            List<LinkedHashMap<String, Object>> list = objectMapper.readValue(jsonInput, new TypeReference<List<LinkedHashMap<String, Object>>>() {
            });
            if (FundUtils.isNull(list))
                throw new RuntimeException("there is no schema for connection");
            int count = 0;
            for (LinkedHashMap<String, Object> map : list) {
                String schemaName = map.get("schema_name").toString();
                log.info("try connection to schema : " + schemaName + "at " + TimeUtils.getNowTime());
                DataSource tenantDataSource = createTenantDataSource(schemaName
                        , map.get("db_url").toString()
                        , map.get("schema_pass").toString());
                if (!FundUtils.isNull(tenantDataSource)) {
                    tenantDataSources.put(map.get(Consts.DATASOURCE_MAP_KEY).toString(), tenantDataSource);
                    log.info(String.format("connection to schema %s was established at %s", schemaName, TimeUtils.getNowTime()));
                }
                count++;
            }
            log.info(String.format("connection established with %s schemas", count));
        } catch (Exception e) {
            log.info("initializeSchemas has error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DataSource createTenantDataSource(String schemaName, String url, String password) {
        try {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
            dataSource.setJdbcUrl(Consts.JDBC_URL_PREFIX.concat(url));
            dataSource.setUsername(schemaName);
            dataSource.setPassword(password);
            dataSource.setPoolName(schemaName);
            dataSource.setMinimumIdle(minimumIdle);
            dataSource.setMaximumPoolSize(maximumPoolSize);
            dataSource.setConnectionTimeout(connectionTimeout);
            dataSource.setIdleTimeout(idleTimeout);
            dataSource.setInitializationFailTimeout(initializationFailTimeout);
            dataSource.setRegisterMbeans(true);
            if (FundUtils.executeQuery(dataSource, "select 1 from dual"))
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

    @PostConstruct
    public void init() {
        if (profileService.isDebugMode())
            initializeSchemas();
        else
            initializeSchemas(dataSource);
    }

    public Map<String, DataSource> getTenantDataSources() {
        return tenantDataSources;
    }
}

