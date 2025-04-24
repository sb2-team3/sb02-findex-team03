package com.findex.demo.indexData.datas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexPerformanceDto {

    private Integer indexInfoId;
    private String indexClassification;
    private String indexName;
    private double versus;
    private double fluctuationRate;
    private double currentPrice;
    private double beforePrice;
}
