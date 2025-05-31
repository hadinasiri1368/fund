package org.fund.paymentModule.payment.constant;

import java.util.Arrays;
import java.util.Objects;

public enum PaymentType {
    CASH(1L, "نقد"),
    CHECK(2L, "چك"),
    DEPOSIT(3L, "واریز"),
    REMITTANCE(4L, "حواله"),
    TRANSFER(5L, "انتقال"),
    WITHDRAWAL(6L, "برداشت"),
    GROUP_CHECK(7L, "چك گروهي"),
    GROUP_WITHDRAWAL_ONLINE(8L, "برداشت گروهي آنلاين"),
    GROUP_TRANSFER_ONLINE(9L, "برداشت انتقال آنلاين");

    private final Long id;
    private final String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    PaymentType(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static PaymentType getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
