package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    boolean favorite
) {

}
