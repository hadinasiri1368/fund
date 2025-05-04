package org.fund.baseInformation.tradableItem;

import java.util.Arrays;
import java.util.Objects;

public enum TradableItemGroup {
    INSTRUMENT(1, "نماد"),
    CONTRACT(2, "قرارداد"),
    FUND(3, "صندوق");

    private final Integer id;
    private final String title;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    TradableItemGroup(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static TradableItemGroup getItemById(Integer id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
