package org.fund.common;

import org.fund.constant.Consts;
import org.fund.exception.FundException;
import org.fund.exception.GeneralExceptionType;

import java.time.LocalDate;
import java.util.regex.Pattern;

public class DateUtils {
    private static final Pattern PATTERN = Pattern.compile(Consts.PERSIAN_DATE_REGEX);

    public static LocalDate convertToGregorian(String persianDate) {
        if (persianDate == null || persianDate.trim().isEmpty()) {
            throw new FundException(GeneralExceptionType.DATE_CANNOT_BE_NULL);
        }


        if (!PATTERN.matcher(persianDate).matches()) {
            throw new FundException(GeneralExceptionType.DATE_CANNOT_BE_NULL, new Object[]{"تاریخ"});
        }

        try {
            String[] parts = persianDate.split("/");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int day = Integer.parseInt(parts[2]);

            return toGregorian(year, month, day);
        } catch (Exception e) {
            throw new IllegalArgumentException("تاریخ وارد شده نامعتبر است: " + e.getMessage(), e);
        }
    }


    private static LocalDate toGregorian(int persianYear, int persianMonth, int persianDay) {
        // ثابت‌های مورد نیاز برای محاسبات
        int[] persianMonthDays = {31, 31, 31, 31, 31, 31, 30, 30, 30, 30, 30, 29};
        int persianEpoch = 226894; // تعداد روزهای میلادی تا ابتدای تقویم شمسی
        int[] gregorianMonthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        // محاسبه روز شمسی از ابتدای تقویم شمسی
        int persianDayOfYear = 0;
        for (int i = 0; i < persianMonth - 1; i++) {
            persianDayOfYear += persianMonthDays[i];
        }
        persianDayOfYear += persianDay;

        // بررسی سال کبیسه در تقویم شمسی
        if (isPersianLeapYear(persianYear) && persianMonth == 12) {
            persianMonthDays[11] = 30;
        }

        // تبدیل تاریخ شمسی به تعداد روزهای میلادی
        int totalPersianDays = (persianYear - 1) * 365 + (persianYear - 1) / 4 + persianDayOfYear + persianEpoch;

        // تبدیل تعداد روزها به تاریخ میلادی
        int gregorianYear = 1;
        while (totalPersianDays > 365) {
            if (isGregorianLeapYear(gregorianYear)) {
                if (totalPersianDays > 366) {
                    totalPersianDays -= 366;
                    gregorianYear++;
                } else {
                    break;
                }
            } else {
                totalPersianDays -= 365;
                gregorianYear++;
            }
        }

        // محاسبه ماه و روز میلادی
        int gregorianMonth = 1;
        for (int i = 0; i < 12; i++) {
            int daysInMonth = gregorianMonthDays[i];
            if (i == 1 && isGregorianLeapYear(gregorianYear)) {
                daysInMonth = 29;
            }
            if (totalPersianDays > daysInMonth) {
                totalPersianDays -= daysInMonth;
                gregorianMonth++;
            } else {
                break;
            }
        }

        int gregorianDay = totalPersianDays;

        // بازگشت تاریخ میلادی
        return LocalDate.of(gregorianYear, gregorianMonth, gregorianDay);
    }

    // بررسی سال کبیسه در تقویم شمسی
    private static boolean isPersianLeapYear(int year) {
        int mod = year % 33;
        return mod == 1 || mod == 5 || mod == 9 || mod == 13 || mod == 17 || mod == 22 || mod == 26 || mod == 30;
    }

    // بررسی سال کبیسه در تقویم میلادی
    private static boolean isGregorianLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static boolean isValid(String persianDate) {
        try {
            convertToGregorian(persianDate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
