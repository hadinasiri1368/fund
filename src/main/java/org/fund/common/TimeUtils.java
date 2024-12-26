package org.fund.common;

import org.fund.constant.TimeFormat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    public static String getNowTime() {
        return getNowTime(null);
    }

    public static String getNowTime(TimeFormat timeFormat) {
        LocalTime currentTime = LocalTime.now();
        if (FundUtils.isNull(timeFormat))
            timeFormat = TimeFormat.HOUR_MINUTE_SECOND;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat.getValue());
        return currentTime.format(formatter);
    }
}
