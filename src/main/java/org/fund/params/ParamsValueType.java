package org.fund.params;

import java.util.Arrays;
import java.util.Objects;

public enum ParamsValueType {
    NUMBER(1, "NUMBER"),
    STRING(2, "STRING"),
    BOOLEAN(3, "BOOLEAN"),
    DATE(4, "DATE"),
    DROPDOWN(5, "DROPDOWN"),
    TIME(6, "TIME");

    private final Integer id;
    private final String title;

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    ParamsValueType(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ParamsValueType getItemById(Integer id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
