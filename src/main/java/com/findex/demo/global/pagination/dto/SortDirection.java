package com.findex.demo.global.pagination.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortDirection {
    ASC("asc"),
    DESC("desc");

    private final String value;

    @JsonCreator
    public static SortDirection from(String value) {
        for (SortDirection dir : SortDirection.values()) {
            if (dir.getValue().equalsIgnoreCase(value)) {
                return dir;
            }
        }
        throw new IllegalArgumentException("Invalid sortDirection: " + value);
    }
}
