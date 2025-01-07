package org.fund.config.cache;


import lombok.extern.slf4j.Slf4j;
import org.fund.constant.Consts;
import org.fund.model.BaseEntity;
import org.fund.repository.JpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CacheService {
    private final JpaRepository jpaRepository;

    public CacheService(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Cacheable(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> List<ENTITY> findAll(Class<ENTITY> entityClass) {
        return jpaRepository.findAll(entityClass);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void save(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.save(entity, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void update(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.update(entity, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY, ID> void removeById(Class<ENTITY> entityClass, ID id, Long userId, String uuid) throws Exception {
        jpaRepository.removeById(entityClass, id, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY, ID> void remove(ENTITY entity, Long userId, String uuid) throws Exception {
        jpaRepository.remove(entity, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void batchInsert(List<ENTITY> entities, Long userId, String uuid) throws Exception {
        jpaRepository.batchInsert(entities, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void batchUpdate(List<ENTITY> entities, Long userId, String uuid) throws Exception {
        jpaRepository.batchUpdate(entities, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> void batchRemove(List<ENTITY> entities, Long userId, String uuid) throws Exception {
        jpaRepository.batchRemove(entities, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> int executeUpdate(Class<ENTITY> entityClass, String sql, Long userId, String uuid) throws Exception {
        return jpaRepository.executeUpdate(sql, userId, uuid);
    }

    @CacheEvict(value = Consts.CACHE_NAME, keyGenerator = "tenantKeyGenerator")
    public <ENTITY> int executeUpdate(Class<ENTITY> entityClass, String sql, Map<String, Object> params, Long userId, String uuid) throws Exception {
        return jpaRepository.executeUpdate(sql, params, userId, uuid);
    }
}
