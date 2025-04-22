package com.findex.demo.autoSyncConfig.domain.dto;

public record AutoSyncConfigDto(
    Integer id,
    Integer  indexInfoId,
    String indexClassification,
    String indexName,
    Boolean enabled
) {
}
