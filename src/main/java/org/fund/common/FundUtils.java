package org.fund.common;

import jakarta.servlet.http.HttpServletRequest;
import org.fund.constant.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.net.URI;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class FundUtils extends CommonUtils {
    private static MessageSource messageSource;
    private static final Pattern mobile_pattern = Pattern.compile(Consts.MOBILE_REGEX);
    private static final Pattern email_pattern = Pattern.compile(Consts.EMAIL_REGEX);

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        FundUtils.messageSource = messageSource;
    }

    public static String getMessage(String key) {
        return getMessage(key, null);
    }

    public static String getMessage(String key, Object... params) {
        return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

    public static String getToken(HttpServletRequest request) {
        if (isNull(request.getHeader("Authorization")))
            return null;
        return request.getHeader("Authorization").replaceAll("Bearer ", "");
    }

    public static boolean checkNationalCode(String nationalCode) {
        if (isNull(nationalCode) || nationalCode.length() != 10) {
            return false;
        }

        try {

            int controlDigit = FundUtils.longValue(nationalCode.substring(9, 10)).intValue();

            int sum = 0;
            for (int i = 0; i < 9; i++) {
                int digit = FundUtils.longValue(nationalCode.substring(i, i + 1)).intValue();
                sum += digit * (10 - i);
            }

            int remainder = sum % 11;

            if (remainder < 2)
                return controlDigit == remainder;
            else
                return controlDigit == (11 - remainder);
        } catch (Exception e) {
            return false;
        }
    }

    public static Double doubleValue(Object number) {
        if (number instanceof Number)
            return ((Number) number).doubleValue();
        else
            try {
                return Double.valueOf(number.toString().trim());
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public static Float floatValue(Object number) {
        if (number instanceof Number)
            return ((Number) number).floatValue();
        else
            try {
                return Float.valueOf(number.toString().trim());
            } catch (NumberFormatException e) {
                return null;
            }
    }

    public static boolean booleanValue(Object input) {
        if (isNull(input))
            return false;
        String value = input.toString().trim();
        return value.equalsIgnoreCase("true") ||
                value.equals("1") ||
                value.equalsIgnoreCase("yes") ||
                value.equalsIgnoreCase("on");
    }

    public static boolean isMobileValid(String mobile) {
        return mobile_pattern.matcher(mobile).matches();
    }

    public static boolean isEmailValid(String email) {
        return email_pattern.matcher(email).matches();
    }

    public static String encodePassword(String password) {
        return toSHA1(md5(password).getBytes());
    }

}
