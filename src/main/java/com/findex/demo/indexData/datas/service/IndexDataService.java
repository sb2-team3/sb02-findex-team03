package com.findex.demo.indexData.datas.service;

import com.findex.demo.indexData.datas.domain.dto.IndexChartDto;
import com.findex.demo.indexData.datas.domain.dto.IndexPerformanceDto;
import com.findex.demo.indexData.datas.domain.dto.PeriodType;
import com.findex.demo.indexData.datas.domain.dto.RankedIndexPerformanceDto;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IndexDataService {

    public List<IndexChartDto> getIndexChart(PeriodType periodType, int indexInfoId) {

        return null;
    }

    public List<RankedIndexPerformanceDto> getIndexPerformanceRank(PeriodType periodType,
        Integer indexInfoId, Integer limit) {
        return null;
    }

    public List<IndexPerformanceDto> getFavoriteIndexPerformanceRank(PeriodType periodType) {
        return null;
    }
}
