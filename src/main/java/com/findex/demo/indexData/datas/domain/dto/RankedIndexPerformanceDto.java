package com.findex.demo.indexData.datas.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RankedIndexPerformanceDto {

    private Performance performance;
    private int rank;
}
