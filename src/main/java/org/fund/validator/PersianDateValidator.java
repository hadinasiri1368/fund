package org.fund.validator;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fund.common.DateUtils;
import org.fund.common.FundUtils;
import org.fund.constant.Consts;

import java.util.regex.Pattern;

public class PersianDateValidator implements ConstraintValidator<ValidPersianDate, String> {
    private static final Pattern PATTERN = Pattern.compile(Consts.PERSIAN_DATE_REGEX);
    private ValidPersianDate annotation;


    @Override
    public void initialize(ValidPersianDate constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        boolean isValid = true;
        if (FundUtils.isNull(value)) {
            return true;
        }
        if (!PATTERN.matcher(value).matches()) {
            isValid = false;
        } else if (!DateUtils.isValid(value)) {
            isValid = false;
        }
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(annotation.message() + "&" + annotation.fieldName()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
