package com.findex.demo.indexInfo.domain.dto;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexInfoCreateRequest(
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    Boolean favorite
) {

}