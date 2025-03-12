package org.fund.administration.params.constant;

import java.util.Arrays;
import java.util.Objects;

public enum ParamType {
    ACCOUNT(1L, "حساب ها"),
    CALCULATION_COEFFICIENTS(2L, "ضرايب محاسباتي"),
    USER_INTERFACE(3L, "رابط کاربر"),
    SYSTEM_PARAMETERS(4L, "پارامترهاي سيستمي"),
    SERVICE(5L, "وب سرويس"),
    OPERATIONAL_PARAMETERS(6L, "پارامترهاي عملياتي"),
    SETTING_PROFILE(7L, "تنظیم مشخصات"),
    ACCOUNT_SETUP(8L, "تنظیم حساب");

    private final Long id;
    private final String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    ParamType(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ParamType getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
