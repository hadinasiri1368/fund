package org.fund.accounting.detailLedger.constant;

import java.util.Arrays;
import java.util.Objects;

public enum AccountNature {
    DEBIT(1L, "بدهکار"),
    CREDIT(2L, "بستانكار"),
    DEBIT_CREDIT(3L, "بد/بس"),
    BASED_ON_CATEGORY(4L, "طبق طبقه");

    private final Long id;
    private final String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    AccountNature(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static AccountNature getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
