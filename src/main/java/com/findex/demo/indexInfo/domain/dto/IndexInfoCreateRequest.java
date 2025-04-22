package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDatetime basePointInTime,
    int baseIndex,
    boolean favorite
) {

}
