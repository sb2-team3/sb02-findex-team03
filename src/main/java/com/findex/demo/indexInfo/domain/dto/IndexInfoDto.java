package com.findex.demo.indexInfo.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.LocalDate;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    SourceType sourceType,
    boolean favorite
) {}