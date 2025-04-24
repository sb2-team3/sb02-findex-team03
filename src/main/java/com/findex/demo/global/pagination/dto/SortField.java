package com.findex.demo.global.pagination.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SortField {
    INDEX_NAME("indexInfo.indexName"),
    ENABLED("enabled"),
    INDEX_INFO_ID("indexInfo.id");


    private final String value;

    @JsonCreator
    public static SortField from(String value) {
        for (SortField field : SortField.values()) {
            if (field.getValue().equalsIgnoreCase(value)) {
                return field;
            }
        }
        throw new IllegalArgumentException("Invalid sortField: " + value);
    }

}
