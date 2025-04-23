package com.findex.demo.indexData.datas.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankedIndexPerformanceDto {

    private Performance performance;
    private int rank;
}
