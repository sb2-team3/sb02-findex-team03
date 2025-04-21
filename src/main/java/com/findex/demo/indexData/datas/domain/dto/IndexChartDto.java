package com.findex.demo.indexData.datas.domain.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IndexChartDto {

    private long indexInfoId;
    private String indexClassification;
    private String indexName;
    private PeriodType periodType;
    private List<Items> dataPoints;
    private List<Items> ma5DataPoints;
    private List<Items> ma20DataPoints;
}
