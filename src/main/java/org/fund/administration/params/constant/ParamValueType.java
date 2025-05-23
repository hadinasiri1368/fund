package org.fund.administration.params.constant;

import java.util.Arrays;
import java.util.Objects;

public enum ParamValueType {
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

    ParamValueType(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public static ParamValueType getItemById(Integer id) {
        return Arrays.stream(values())
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElse(null);
    }
}
