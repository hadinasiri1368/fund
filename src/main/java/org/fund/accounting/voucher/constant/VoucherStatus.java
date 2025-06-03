package org.fund.accounting.voucher.constant;

import java.util.Arrays;
import java.util.Objects;

public enum VoucherStatus {
    DRAFT(1L, "پيش نويس"),
    TEMPORARY(2L, "موقت"),
    REVIEW(3L, "بررسي"),
    DEFINITE(4L, "قطعي");

    private final Long id;
    private final String title;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    VoucherStatus(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public static VoucherStatus getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
