package com.findex.demo.indexInfo.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record IndexInfoDto(
    Integer id,
    String indexClassification,
    String indexName,
    int employedItemsCount,
    LocalDate basePointInTime,
    int baseIndex,
    SourceType sourceType,
    boolean favorite
) {}
