package org.fund.config.dataBase;

import org.fund.common.FundUtils;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class CustomTenantIdentifierResolver implements CurrentTenantIdentifierResolver {
    private final TenantDataSourceManager tenantDataSourceManager;

    public CustomTenantIdentifierResolver(TenantDataSourceManager tenantDataSourceManager) {
        this.tenantDataSourceManager = tenantDataSourceManager;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {//
        if (FundUtils.isNull(TenantContext.getCurrentTenant())) {
            return tenantDataSourceManager.getTenantDataSources().keySet().stream().findFirst().get();
        }
        return TenantContext.getCurrentTenant();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
