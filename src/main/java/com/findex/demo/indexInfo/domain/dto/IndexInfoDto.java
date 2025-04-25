package com.findex.demo.indexInfo.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record IndexInfoDto(
    Integer id,
    String indexClassification,
    String indexName,
    Integer employedItemsCount,
    LocalDate basePointInTime,
    Integer baseIndex,
    SourceType sourceType,
    Boolean favorite
) {

}