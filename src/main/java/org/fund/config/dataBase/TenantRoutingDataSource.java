package org.fund.config.dataBase;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

@Component
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        // Return the schema name based on current request or tenant context
        return TenantContext.getCurrentDataSource();
    }
}

