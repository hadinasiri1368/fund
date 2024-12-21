package org.fund.config.dataBase;

import javax.sql.DataSource;

public class TenantContext {

    private static ThreadLocal<DataSource> currentDataSource = new ThreadLocal<>();

    public static void setCurrentDataSource(DataSource dataSource) {
        currentDataSource.set(dataSource);
    }

    public static DataSource getCurrentDataSource() {
        return currentDataSource.get();
    }
}

