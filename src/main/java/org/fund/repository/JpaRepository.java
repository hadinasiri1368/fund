package org.fund.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.fund.common.CommonUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;
import org.fund.constant.OperationType;
import org.fund.constant.TimeFormat;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;
import org.fund.model.BaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public class JpaRepository {
    @Value("${spring.jpa.show-manual-log}")
    private Boolean showManualLog;

    private static final Logger log = LoggerFactory.getLogger(JpaRepository.class);
    @PersistenceContext()
    private EntityManager entityManager;

    @Transactional
    public <ENTITY> void save(ENTITY entity, Long userId, String uuid) throws Exception {
        ((BaseEntity) entity).setId(null);
        ((BaseEntity) entity).setInsertedUserId(userId);
        ((BaseEntity) entity).setInsertedDateTime(new Date());

        FundUtils.setNull(entity);
        entityManager.persist(entity);
        logEntityFields(entity, OperationType.INSERT, userId, uuid);
    }

    @Transactional
    public <ENTITY> void update(ENTITY entity, Long userId, String uuid) throws Exception {
        if (FundUtils.isNull(((BaseEntity) entity).getId()))
            throw new FundException(GeneralExceptionType.ID_IS_NULL);
        ((BaseEntity) entity).setUpdatedUserId(userId);
        ((BaseEntity) entity).setUpdatedDateTime(new Date());

        FundUtils.setNull(entity);
        entityManager.merge(entity);
        logEntityFields(entity, OperationType.UPDATE, userId, uuid);
    }

    @Transactional
    public <ENTITY> void remove(ENTITY entity, Long userId, String uuid) throws Exception {
        logEntityFields(entity, OperationType.DELETE, userId, uuid);
        entityManager.remove(entityManager.merge(entity));
    }

    @Transactional
    public <ENTITY, ID> void removeById(Class<ENTITY> entityClass, ID id, Long userId, String uuid) throws Exception {
        ENTITY entity = findOne(entityClass, id);
        remove(entity, userId, uuid);
    }

    @Transactional
    public int executeUpdate(String sql, Map<String, Object> param, Long userId, String uuid) {
        Query query = entityManager.createQuery(sql);
        if (!FundUtils.isNull(param) && !param.isEmpty()) {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }
        logQueryWithParameters(sql, param, userId, uuid);
        return query.executeUpdate();
    }

    @Transactional
    public int executeUpdate(String sql, Long userId, String uuid) {
        return executeUpdate(sql, null, userId, uuid);
    }

    @Transactional
    public <ENTITY> void batchInsert(List<ENTITY> entities, Long userId, String uuid) throws Exception {
        if (FundUtils.isNull(entities) || entities.size() == 0)
            return;
        int batchSize = Consts.JPA_BATCH_SIZE;
        for (int i = 0; i < entities.size(); i++) {
            save(entities.get(i), userId, uuid);
//            entityManager.persist(entities.get(i));

            if (i > 0 && i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Transactional
    public <ENTITY> void batchUpdate(List<ENTITY> entities, Long userId, String uuid) throws Exception {
        if (FundUtils.isNull(entities) || entities.size() == 0)
            return;
        int batchSize = Consts.JPA_BATCH_SIZE;
        for (int i = 0; i < entities.size(); i++) {
//            entityManager.merge(entities.get(i));
            update(entities.get(i), userId, uuid);

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

    private <ENTITY> void logEntityFields(ENTITY entity, OperationType operation, Long userId, String uuid) {
        if (!showManualLog)
            return;

        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));

        if (entity == null) {
            log.info("Entity is null. UserId: {}, Time: {}, uuid: {}", userId, currentTime, uuid);
            return;
        }

        Class<?> entityClass = entity.getClass();
        StringBuilder logMessage = new StringBuilder(operation.getValue() + " entity : ")
                .append(entityClass.getAnnotation(Entity.class).name()).append(" [");

        Field[] fields = entityClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                Object value = field.get(entity);
                logMessage.append(field.getName()).append("=").append(value);
            } catch (IllegalAccessException e) {
                logMessage.append(field.getName()).append("=ACCESS_ERROR");
            }
            if (i < fields.length - 1) {
                logMessage.append(", ");
            }
        }

        logMessage.append("]");

        String finalLogMessage = String.format("%s | UserId: %d | Time: %s | uuid: %s",
                logMessage, userId, currentTime, uuid);

        log.info(finalLogMessage);
    }


    private void logQueryWithParameters(String sql, Map<String, Object> param, Long userId, String uuid) {
        if (!showManualLog)
            return;

        String currentTime = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern(Consts.GREGORIAN_DATE_FORMAT + " " + TimeFormat.HOUR_MINUTE_SECOND.getValue()));

        StringBuilder logMessage = new StringBuilder(sql);
        if (param != null && !param.isEmpty()) {
            logMessage.append(" with parameters: [");
            param.forEach((key, value) -> logMessage.append(key).append("=").append(value).append(", "));
            logMessage.setLength(logMessage.length() - 2);
            logMessage.append("]");
        }

        logMessage.append(" | ")
                .append("UserId: ")
                .append(userId)
                .append(" | ")
                .append("Time: ")
                .append(currentTime)
                .append(" | ")
                .append("uuid: ")
                .append(uuid);

        log.info(logMessage.toString());
    }

}
