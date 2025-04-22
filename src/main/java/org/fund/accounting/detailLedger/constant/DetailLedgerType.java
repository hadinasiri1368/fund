package org.fund.accounting.detailLedger.constant;

import java.util.Arrays;
import java.util.Objects;

public enum DetailLedgerType {
    CUSTOMER(1L, "سرمايه گذاران", AccountNature.DEBIT_CREDIT),
    BANK(2L, "بانک", AccountNature.CREDIT),
    FUND(3L, "صندوق", AccountNature.DEBIT),
    EMPLOYEE(4L, "کارکنان", AccountNature.DEBIT_CREDIT),
    ASSETS(5L, "اموال", AccountNature.DEBIT),
    COST_CENTER(6L, "مرکز هزینه", AccountNature.DEBIT_CREDIT),
    PUBLIC_COMPANY(7L, "شركت بورسي", AccountNature.DEBIT_CREDIT),
    BROKERAGE(8L, "كارگزاري", AccountNature.BASED_ON_CATEGORY),
    OTHER(9L, "ساير", AccountNature.DEBIT_CREDIT),
    FINANCIAL_COMPANY(10L, "نهاد مالی", AccountNature.BASED_ON_CATEGORY);
    private final Long id;
    private final String name;
    private final AccountNature accountNature;

    DetailLedgerType(Long id, String name, AccountNature accountNature) {
        this.id = id;
        this.name = name;
        this.accountNature = accountNature;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccountNature getAccountNature() {
        return accountNature;
    }

    public static DetailLedgerType getItemById(Long id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
