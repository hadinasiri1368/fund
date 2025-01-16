package org.fund.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fund.common.FundUtils;

public class NationalCodeValidator implements ConstraintValidator<ValidNationalCode, String> {
    private ValidNationalCode annotation;

    @Override
    public void initialize(ValidNationalCode constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String string, ConstraintValidatorContext context) {
        if(!FundUtils.checkNationalCode(string)){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(annotation.message()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
