package org.fund.config.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.model.BaseEntity;
import org.fund.model.Fund;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Component
public class CacheAspect {
    private final CacheService cacheService;
    private final EntityNameToClassMapper entityNameToClassMapper;
    private static ThreadLocal<Boolean> inCacheService = ThreadLocal.withInitial(() -> false);

    public CacheAspect(CacheService cacheService, EntityNameToClassMapper entityNameToClassMapper) {
        this.cacheService = cacheService;
        this.entityNameToClassMapper = entityNameToClassMapper;
    }

    @Around("execution(* org.fund.repository.JpaRepository.*(..))")
    public Object handleCacheableEntities(ProceedingJoinPoint joinPoint) throws Throwable {
        if (inCacheService.get())
            return joinPoint.proceed();
        try {
            inCacheService.set(true);
            Object entity = joinPoint.getArgs()[0];
            if (entity != null) {
                String methodName = joinPoint.getSignature().getName();
                if (methodName.equals("executeUpdate")) {
                    String tableName = FundUtils.extractTableNameFromSql(entity.toString());
                    Class<?> entityClass = entityNameToClassMapper.getEntityClass(tableName);
                    if (joinPoint.getArgs().length == 3)
                        return cacheService.executeUpdate(entityClass, entity.toString(), RequestContext.getUserId(), RequestContext.getUuid());
                    return cacheService.executeUpdate(entityClass, entity.toString(), (Map) joinPoint.getArgs()[1], RequestContext.getUserId(), RequestContext.getUuid());
                }
                if (Class.forName(FundUtils.getClassName(entity)).isAnnotationPresent(CacheableEntity.class)) {
                    if ("save".equals(methodName)) {
                        cacheService.save(entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("update".equals(methodName)) {
                        cacheService.update(entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("remove".equals(methodName)) {
                        cacheService.remove(entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("removeById".equals(methodName)) {
                        Long id = FundUtils.longValue(joinPoint.getArgs()[1]);
                        cacheService.removeById((Class<?>) entity, id, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("findOne".equals(methodName)) {
                        Long id = FundUtils.longValue(joinPoint.getArgs()[1]);
                        return cacheService.findAll((Class<?>) entity).stream()
                                .filter(a -> (getId(a)).equals(id))
                                .findFirst()
                                .orElse(null);
                    } else if ("findAll".equals(methodName)) {
                        return cacheService.findAll((Class<?>) entity);
                    } else if ("findAllByPage".equals(methodName)) {
                        return cacheService.findAllByPage((Class<?>) entity, (Pageable) joinPoint.getArgs()[1]);
                    } else if ("batchInsert".equals(methodName)) {
                        cacheService.batchInsert((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("batchUpdate".equals(methodName)) {
                        cacheService.batchUpdate((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("batchRemove".equals(methodName)) {
                        cacheService.batchRemove((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("findBy".equals(methodName)) {
                        String filedName = joinPoint.getArgs()[1].toString();
                        String value = joinPoint.getArgs()[2].toString();
                        return cacheService.findAll((Class<?>) entity).stream()
                                .filter(a -> getFieldValue(a, filedName).toString().equals(value))
                                .toList();
                    }

                }
            }
            return joinPoint.proceed();
        } finally {
            inCacheService.remove();
        }
    }

    private <ENTITY> Long getId(ENTITY entity) {
        return FundUtils.longValue(getFieldValue(entity, "id"));
    }

    private <ENTITY> Object getFieldValue(ENTITY entity, String fieldPath) {
        try {
            String[] fields = fieldPath.split("\\.");
            Object currentObject = entity;
            for (String fieldName : fields) {
                if (currentObject == null)
                    return null;
                Method getter = currentObject.getClass()
                        .getMethod("get" + capitalizeFirstLetter(fieldName));
                currentObject = getter.invoke(currentObject);
            }
            return currentObject;
        } catch (Exception e) {
            return null;
        }
    }

    private String capitalizeFirstLetter(String str) {
        return str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}

