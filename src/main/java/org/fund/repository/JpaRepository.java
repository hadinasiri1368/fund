package org.fund.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

@Repository
public class JpaRepository {
    @PersistenceContext()
    private EntityManager entityManager;

    @Transactional
    public <ENTITY> void save(ENTITY entity) throws Exception {
        FundUtils.setNull(entity);
        entityManager.persist(entity);
    }

    @Transactional
    public <ENTITY> void update(ENTITY entity) throws Exception {
        FundUtils.setNull(entity);
        entityManager.merge(entity);
    }

    @Transactional
    public <ENTITY> void remove(ENTITY entity) {
        entityManager.remove(entityManager.merge(entity));
    }

    @Transactional
    public <ENTITY, ID> void removeById(Class<ENTITY> entityClass, ID id) {
        entityManager.remove(entityManager.merge(findOne(entityClass, id)));
    }

    @Transactional
    public int executeUpdate(String sql, Map<String, Object> param) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && !param.isEmpty())
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        return query.executeUpdate();
    }

    @Transactional
    public <ENTITY> void batchInsert(List<ENTITY> entities) {
        if (FundUtils.isNull(entities) || entities.size() == 0)
            return;
        int batchSize = Consts.JPA_BATCH_SIZE;
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public <ENTITY> void batchUpdate(List<ENTITY> entities) {
        if (FundUtils.isNull(entities) || entities.size() == 0)
            return;
        int batchSize = Consts.JPA_BATCH_SIZE;
        for (int i = 0; i < entities.size(); i++) {
            entityManager.persist(entities.get(i));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    public <ENTITY, ID> ENTITY findOne(Class<ENTITY> entityClass, ID id) {
        return entityManager.find(entityClass, id);
    }

    public <ENTITY> Page<ENTITY> findAll(Class<ENTITY> entityClass, Pageable pageable) {
        Entity entity = entityClass.getAnnotation(Entity.class);
        Query query = entityManager.createQuery("select entity from " + entity.name() + " entity");
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<ENTITY> resultList = query.getResultList();
        long totalCount = getTotalCount(entity.name());
        return new PageImpl<>(resultList, pageable, totalCount);
    }

    public <ENTITY> List<ENTITY> findAll(Class<ENTITY> entityClass) {
        Entity entity = entityClass.getAnnotation(Entity.class);
        Query query = entityManager.createQuery("select entity from " + entity.name() + " entity");
        return query.getResultList();
    }

    public List listByQuery(String sql, Map<String, Object> param) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && param.size() > 0)
            for (String key : param.keySet()) {
                query.setParameter(key, param.get(key));
            }
        return query.getResultList();
    }

    public Page<Object> listByQuery(String sql, Map<String, Object> param, Pageable pageable) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        long countResult = getTotalCount(query);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        List<Object> resultList = query.getResultList();
        return new PageImpl<>(resultList, pageable, countResult);
    }

    public Page<Object> listByQuery(String sql, Pageable pageable) {
        return listByQuery(sql, null, pageable);
    }

    public Long getLongValue(String sql, Map<String, Object> param) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return (Long) query.getSingleResult();
    }

    public Long getLongValue(String sql) {
        return getLongValue(sql, null);
    }

    public String getStringValue(String sql, Map<String, Object> param) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        return (String) query.getSingleResult();
    }

    public String getStringValue(String sql) {
        return getStringValue(sql, null);
    }

    private long getTotalCount(String entityName) {
        Query countQuery = entityManager.createQuery("select count(entity) from " + entityName + " entity");
        return (long) countQuery.getSingleResult();
    }

    private long getTotalCount(Query query) {
        String queryString = query.unwrap(org.hibernate.query.Query.class).getQueryString();

        int orderByIndex = queryString.toLowerCase().indexOf("order by");
        if (orderByIndex != -1) {
            queryString = queryString.substring(0, orderByIndex);
        }

        String countQueryString = "select count(1) from (" + queryString + ") tbl_count";
        Query countQuery = entityManager.createQuery(countQueryString);

        query.getParameters().forEach(param -> {
            Object value = query.getParameterValue(param);
            countQuery.setParameter(param.getName(), value);
        });

        return (long) countQuery.getSingleResult();
    }
}
