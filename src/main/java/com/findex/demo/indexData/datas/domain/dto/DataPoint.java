package com.findex.demo.indexData.datas.domain.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPoint {

    private LocalDate data;
    private double value;
}
