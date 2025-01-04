package org.fund.config.cache;


import lombok.extern.slf4j.Slf4j;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Slf4j
@Service
public class CacheService {
    private final JpaRepository jpaRepository;
    public CacheService(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Cacheable(value = "findAll", keyGenerator = "tenantKeyGenerator")
    public <ENTITY> List<ENTITY> findAll(Class<ENTITY> entityClass) {
        System.out.println("Caching entity: " + entityClass.getName());
        return jpaRepository.findAll(entityClass);
    }

    public <ENTITY, ID> ENTITY findOne(Class<ENTITY> entityClass, ID id) {
        return findAll(entityClass).stream()
                .filter(a -> {
                    try {
                        Field idField = a.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        Object fieldValue = idField.get(a);
                        return fieldValue != null && fieldValue.equals(id);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.info("cacheService findOne error : {}", e.getMessage());
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    @CacheEvict(value = "findAll", keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void save(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.save(entity, userId, uuid);
    }

    @CacheEvict(value = "findAll", keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void update(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.update(entity, userId, uuid);
    }

    @CacheEvict(value = "findAll", keyGenerator = "tenantKeyGenerator")
    public <ENTITY, ID> void removeById(Class<ENTITY> entityClass, ID id, Long userId, String uuid) throws Exception {
        jpaRepository.removeById(entityClass, id, userId, uuid);
    }

    @CacheEvict(value = "findAll", keyGenerator = "tenantKeyGenerator")
    public <ENTITY, ID> void remove(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.remove(entity, userId, uuid);
    }
}
