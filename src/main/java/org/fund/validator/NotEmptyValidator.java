package org.fund.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fund.common.FundUtils;

public class NotEmptyValidator implements ConstraintValidator<NotEmpty, Object> {
    private NotEmpty annotation;

    @Override
    public void initialize(NotEmpty annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (FundUtils.isNull(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(annotation.message() + "&" + annotation.fieldName()).addConstraintViolation();
            return false;
        }
        return true;
    }

}

