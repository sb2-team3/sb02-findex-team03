package com.findex.demo.indexInfo.domain.dto;

import com.findex.demo.indexInfo.domain.entity.SourceType;
import java.time.Instant;

public record IndexInfoDto(
    Long id,
    String indexClassification,
    String indexName,
    int employedItemCount,
    Instant basePointInTime,
    double baseIndex,
    boolean favorite,
    SourceType sourceType
) {}