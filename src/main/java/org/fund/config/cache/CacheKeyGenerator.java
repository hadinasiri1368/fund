package org.fund.config.cache;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class CacheKeyGenerator {
    public String generateKey(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return String.valueOf(idField.get(entity));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Unable to generate key for entity: " + entity.getClass().getName(), e);
        }
    }
}