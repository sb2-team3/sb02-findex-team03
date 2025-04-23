package com.findex.demo.indexInfo.domain.dto;

import lombok.Builder;

@Builder
public record IndexInfoSummaryDto(
    Integer id,
    String indexClassification,
    String indexName
) {

}