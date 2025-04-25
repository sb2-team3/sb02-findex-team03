package com.findex.demo.indexData.datas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankedIndexPerformanceDto {

    private IndexPerformanceDto performance;
    private int rank;
}
