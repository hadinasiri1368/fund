package org.fund.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.fund.common.FundUtils;
import org.fund.config.cache.CacheableEntity;
import org.fund.repository.JpaRepository;

import java.lang.reflect.Field;
import java.util.List;
@Slf4j
public class ValidateFieldValidator implements ConstraintValidator<ValidateField, Object> {
    private ValidateField annotation;
    private Class<?> entityClass;
    private String fieldName;
    private final JpaRepository jpaRepository;

    public ValidateFieldValidator(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (FundUtils.isNull(value)) {
            return true;
        }

        try {
            if (entityClass.isAnnotationPresent(CacheableEntity.class)) {
                List<?> list = jpaRepository.findAll(annotation.entityClass());

                for (Object entity : list) {
                    Object fieldValue = getFieldValue(entity, fieldName);
                    if (isValueMatch(fieldValue, value)) {
                        return true;
                    }
                }
            } else {
                Object entity = jpaRepository.findOne(entityClass, FundUtils.longValue(value));
                if (entity != null) {
                    Object fieldValue = getFieldValue(entity, fieldName);
                    if (isValueMatch(fieldValue, value)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(
                annotation.message() + "&" + annotation.fieldName()
        ).addConstraintViolation();

        return false;
    }


    private Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Class<?> currentClass = clazz;

        while (currentClass != null) {
            try {
                return currentClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                currentClass = currentClass.getSuperclass();
            }
        }
        throw new NoSuchFieldException("Field '" + fieldName + "' not found in class hierarchy.");
    }

    private Object getFieldValue(Object entity, String fieldName) throws Exception {
        Field field = getFieldFromClassHierarchy(entity.getClass(), fieldName);
        field.setAccessible(true);
        return field.get(entity);
    }

    private boolean isValueMatch(Object fieldValue, Object value) {
        return fieldValue != null && value != null && fieldValue.toString().equals(value.toString());
    }


}