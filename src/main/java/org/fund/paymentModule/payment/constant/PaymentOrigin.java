package org.fund.paymentModule.payment.constant;

import java.util.Arrays;
import java.util.Objects;

public enum PaymentOrigin {
    GROUP_WITHDRAWAL_ONLINE(1L, "برداشت گروهي آنلاین"),
    REQUEST_MANAGEMENT(2L, "مدیریت درخواست ها"),
    SETTLEMENT_WITH_CUSTOMER(3L, "تسویه با مشتریان");

    private final Long id;
    private final String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    PaymentOrigin(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static PaymentOrigin getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
