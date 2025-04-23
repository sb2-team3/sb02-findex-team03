package com.findex.demo.indexData.datas.controller;

import com.findex.demo.indexData.datas.domain.dto.IndexChartDto;
import com.findex.demo.indexData.datas.domain.dto.IndexPerformanceDto;
import com.findex.demo.indexData.datas.domain.dto.PeriodType;
import com.findex.demo.indexData.datas.domain.dto.RankedIndexPerformanceDto;
import com.findex.demo.indexData.datas.service.DataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final DataService indexDataService;

    @GetMapping("api/index-data/{indexInfoId}/chart")
    public ResponseEntity<IndexChartDto> getIndexChart(
        @PathVariable int indexInfoId,
        @RequestParam(required = false, defaultValue = "DAILY") PeriodType periodType
    ) {
        IndexChartDto indexChartDto = indexDataService.getIndexChart(periodType,
            indexInfoId);

        return ResponseEntity.ok(indexChartDto);
    }

    @GetMapping("api/index-data/performance/rank")
    public ResponseEntity<List<RankedIndexPerformanceDto>> getIndexPerformanceRank(
        @RequestParam(required = false, defaultValue = "DAILY") PeriodType periodType,
        @RequestParam(required = false) Integer indexInfoId,
        @RequestParam(required = false, defaultValue = "10") Integer limit
    ) {
        List<RankedIndexPerformanceDto> dto = indexDataService.getIndexPerformanceRank(periodType,
            indexInfoId, limit);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("api/index-data/performance/favorite")
    public ResponseEntity<List<IndexPerformanceDto>> getFavoriteIndexPerformanceRank(
        @RequestParam(required = false, defaultValue = "DAILY") PeriodType periodType
    ) {
        List<IndexPerformanceDto> dto = indexDataService.getInterestIndexPerformance(periodType);

        return ResponseEntity.ok(dto);
    }
}
