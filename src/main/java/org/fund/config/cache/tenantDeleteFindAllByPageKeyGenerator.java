package org.fund.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.fund.common.FundUtils;
import org.fund.config.dataBase.TenantContext;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Slf4j
public class tenantDeleteFindAllByPageKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        String tenantId = TenantContext.getCurrentTenant();
        String key = tenantId + "::" + getKey(params) + ".findAllByPage";
        return key;
    }

    private String getKey(Object... params) {
        if (params != null && params.length > 0) {
            return FundUtils.getClassName(params[0]);
        }
        log.info("tenantKeyGenerator getKey exception : params is null or empty");
        return null;
    }
}

