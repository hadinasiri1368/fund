package org.fund.config.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.fund.common.FundUtils;
import org.fund.config.request.RequestContext;
import org.fund.model.BaseEntity;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

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
                                .filter(a -> ((BaseEntity) a).getId().equals(id))
                                .findFirst()
                                .orElse(null);
                    } else if ("findAll".equals(methodName)) {
                        return cacheService.findAll((Class<?>) entity);
                    } else if ("batchInsert".equals(methodName)) {
                        cacheService.batchInsert((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("batchUpdate".equals(methodName)) {
                        cacheService.batchUpdate((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    } else if ("batchRemove".equals(methodName)) {
                        cacheService.batchRemove((List<?>) entity, RequestContext.getUserId(), RequestContext.getUuid());
                        return null;
                    }
                }
            }
            return joinPoint.proceed();
        } finally {
            inCacheService.remove();
        }
    }
}

