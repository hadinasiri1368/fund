package org.fund.config.dataBase;

import lombok.extern.slf4j.Slf4j;
import org.fund.filter.TenantFilter;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class CustomMultiTenantConnectionProvider implements MultiTenantConnectionProvider {
    private static final Logger log = LoggerFactory.getLogger(CustomMultiTenantConnectionProvider.class);
    private final DataSource dataSource;

    public CustomMultiTenantConnectionProvider(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection(Object object) throws SQLException {
        Connection connection = dataSource.getConnection();
        connection.setSchema(object.toString());
        return connection;
    }

    @Override
    public Connection getAnyConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void releaseConnection(Object object, Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return true;
    }

    @Override
    public boolean isUnwrappableAs(Class<?> unwrapType) {
        return unwrapType.isAssignableFrom(CustomMultiTenantConnectionProvider.class);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if (isUnwrappableAs(unwrapType)) {
            return unwrapType.cast(this);
        }
        log.info("Unwrapping " + unwrapType + " as CustomMultiTenantConnectionProvider");
        throw new IllegalArgumentException("Cannot unwrap to " + unwrapType.getName());
    }
}
