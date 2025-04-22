package com.findex.demo.indexData.datas.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IndexChartDto {

    private long indexInfoId;
    private String indexClassification; // 지수 분류
    private String indexName;
    private PeriodType periodType;
    private List<DataPoint> dataPoints;
    private List<DataPoint> ma5DataPoints;
    private List<DataPoint> ma20DataPoints;
}
