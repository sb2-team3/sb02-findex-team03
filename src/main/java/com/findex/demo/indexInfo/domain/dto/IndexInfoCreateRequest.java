package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDateTime;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDateTime basePointInTime,
    int baseIndex,
    boolean favorite
) {

}
