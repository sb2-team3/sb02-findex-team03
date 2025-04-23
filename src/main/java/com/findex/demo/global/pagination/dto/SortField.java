package com.findex.demo.global.pagination.dto;

public enum SortField {
    INDEX_NAME("indexInfo.indexName"),
    ENABLED("enabled");

    private final String path;

    SortField(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
