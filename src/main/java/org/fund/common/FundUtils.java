package org.fund.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class FundUtils extends CommonUtils {
    private static MessageSource messageSource;

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

    public static Long getUserId(String token) {
        return 123456L;
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
}
