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
    private final JpaRepository jpaRepository;

    public ValidateFieldValidator(JpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void initialize(ValidateField constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (FundUtils.isNull(value)) {
            return true;
        }
        if (annotation.entityClass().isAnnotationPresent(CacheableEntity.class)) {
            List<?> list = jpaRepository.findAll(annotation.entityClass());
            try {
                for (Object entity : list) {
                    Field field = getFieldFromClassHierarchy(entity.getClass(), annotation.fieldName());
                    field.setAccessible(true);
                    Object fieldValue = field.get(entity);
                    if (!FundUtils.isNull(fieldValue) && fieldValue.toString().equals(value.toString())) {
                        return true;
                    }
                }
                valid = false;
            } catch (Exception e) {
                valid = false;
            }
        } else {
            try {
                Object object = jpaRepository.findOne(annotation.entityClass(), FundUtils.longValue(value));
                Field field = getFieldFromClassHierarchy(annotation.entityClass(), annotation.fieldName());
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (!FundUtils.isNull(fieldValue) && fieldValue.toString().equals(value.toString())) {
                    return true;
                }
            } catch (Exception e) {
                valid = false;
            }
        }
        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(annotation.message() + "&" + annotation.fieldName()).addConstraintViolation();
            return false;
        }
        return true;
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

}