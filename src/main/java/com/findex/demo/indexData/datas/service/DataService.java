package com.findex.demo.indexData.datas.service;

import com.findex.demo.indexData.datas.domain.dto.DataPoint;
import com.findex.demo.indexData.datas.domain.dto.IndexChartDto;
import com.findex.demo.indexData.datas.domain.dto.IndexPerformanceDto;
import com.findex.demo.indexData.datas.domain.dto.PeriodType;
import com.findex.demo.indexData.datas.domain.dto.RankedIndexPerformanceDto;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexData.index.repository.IndexDataRepository;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataService {

    private final IndexInfoRepository indexInfoRepository;
    private final IndexDataRepository dataRepository;

    @Transactional(readOnly = true)
    public IndexChartDto getIndexChart(PeriodType periodType, Integer indexInfoId) {
        LocalDate startDate = calculateStartDate(periodType);
        LocalDate endDate = LocalDate.now();

        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
            .orElseThrow(() -> new NoSuchElementException("[ERROR] index info not found"));

        List<IndexData> indexDataList = dataRepository
            .findByIndexInfoAndBaseDateBetweenOrderByBaseDateAsc(indexInfo, startDate, endDate);

        List<DataPoint> dataPoints = indexDataList.stream()
            .map(indexData -> new DataPoint(indexData.getBaseDate(),
                indexData.getClosePrice())).toList();

        List<DataPoint> ma5DataPoints = calculateMovingAverage(dataPoints, 5);
        List<DataPoint> ma20DataPoints = calculateMovingAverage(dataPoints, 20);

        return new IndexChartDto(
            indexInfoId,
            indexInfo.getIndexClassification(),
            indexInfo.getIndexName(),
            periodType,
            dataPoints,
            ma5DataPoints,
            ma20DataPoints
        );
    }

    // 특정 지수 성과 순위 조회.
    @Transactional(readOnly = true)
    public List<RankedIndexPerformanceDto> getIndexPerformanceRank(PeriodType periodType,
        Integer indexInfoId, int limit) {
        LocalDate startDate = calculateStartDate(periodType);
        LocalDate endDate = LocalDate.now();

        List<IndexPerformanceDto> result = new ArrayList<>();

        List<IndexInfo> indexInfoList = indexInfoRepository.findAll();
        for (IndexInfo indexInfo : indexInfoList) {
            Optional<IndexData> start = dataRepository
                .findFirstByIndexInfoAndBaseDateGreaterThanOrderByBaseDateAsc(indexInfo, startDate);

            Optional<IndexData> end = dataRepository
                .findFirstByIndexInfoAndBaseDateLessThanOrderByBaseDateDesc(indexInfo, endDate);

            if (start.isEmpty() || end.isEmpty()) {
                continue;
            }

            IndexPerformanceDto indexPerformanceDto = createIndexPerformanceDto(
                indexInfo, start.get(), end.get()
            );
            result.add(indexPerformanceDto);
        }

        result.sort(Comparator.comparing(IndexPerformanceDto::getFluctuationRate).reversed());
        List<RankedIndexPerformanceDto> dtos = new ArrayList<>();
        int rank = 0;
        for (IndexPerformanceDto indexPerformanceDto : result) {
            dtos.add(new RankedIndexPerformanceDto(indexPerformanceDto, rank++));
        }

        if (indexInfoId != null) {
            return dtos.stream()
                .filter(dto -> dto.getPerformance().getIndexInfoId().equals(indexInfoId))
                .toList();
        } else {
            return dtos.size() < limit ? dtos : dtos.subList(0, limit);
        }
    }

    private IndexPerformanceDto createIndexPerformanceDto(IndexInfo index, IndexData start,
        IndexData end) {

        Double startPrice = start.getClosePrice();
        Double endPrice = end.getClosePrice();
        double versus = endPrice - startPrice;
        double fluctuationRate = (versus / startPrice) * 100;
        fluctuationRate = Math.round(fluctuationRate * 100) / 100.0;

        return new IndexPerformanceDto(
            index.getId(),
            index.getIndexClassification(),
            index.getIndexName(),
            versus,
            fluctuationRate,
            startPrice,
            endPrice
        );
    }

    @Transactional(readOnly = true)
    public List<IndexPerformanceDto> getInterestIndexPerformance(PeriodType periodType) {
        List<IndexInfo> favoriteIndexes = indexInfoRepository.findByFavoriteIsTrue();
        if (favoriteIndexes.isEmpty()) {
            return Collections.emptyList();
        }

        LocalDate startDate = calculateStartDate(periodType);
        LocalDate actualStartDate = findNearestTradingDay(startDate, false);

        LocalDate endDate = LocalDate.now();
        LocalDate actualEndDate = findNearestTradingDay(endDate, true);

        List<Integer> favoriteIndexIds = favoriteIndexes.stream()
            .map(IndexInfo::getId)
            .toList();

        List<IndexData> indexDataList = dataRepository.findByIndexInfoIdInAndBaseDateIn(
            favoriteIndexIds, List.of(actualStartDate, actualEndDate));

        if (indexDataList.isEmpty()) {
            return Collections.emptyList();
        }

        // IndexInfo 에 대한 성과
        Map<Integer, IndexData> startDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(actualStartDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(),
                Function.identity(), (existing, replacement) -> existing
            ));

        Map<Integer, IndexData> endDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(actualEndDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(),
                Function.identity(), (existing, replacement) -> existing));

        return favoriteIndexes.stream()
            .map(indexInfo -> createIndexPerformanceDto(indexInfo, startDateMap, endDateMap))
            .flatMap(Optional::stream)
            .toList();
    }

    private LocalDate findNearestTradingDay(LocalDate date, boolean findPrevious) {
        boolean hasData = dataRepository.existsByBaseDate(date);

        if (hasData) {
            return date;
        }

        if (findPrevious) {
            Optional<LocalDate> prevDate = dataRepository.findMaxBaseDateBeforeDate(date);
            return prevDate.orElseThrow(() ->
                new NoSuchElementException("No trading day found before " + date));
        } else {
            Optional<LocalDate> nextDate = dataRepository.findMinBaseDateAfterDate(date);
            return nextDate.orElseThrow(() ->
                new NoSuchElementException("No trading day found after " + date));
        }
    }

    private List<DataPoint> calculateMovingAverage(List<DataPoint> dataPoints, int period) {
        List<DataPoint> maLine = new ArrayList<>();

        if (dataPoints.size() < period) {
            return maLine;
        }

        for (int cnt_i = period - 1; cnt_i < dataPoints.size(); cnt_i++) {
            double sum = 0;

            for (int cnt_j = 0; cnt_j < period; cnt_j++) {
                sum += dataPoints.get(cnt_i - cnt_j).getValue();
            }

            double average = sum / period;

            DataPoint maPoint = new DataPoint(
                dataPoints.get(cnt_i).getDate(), average
            );

            maLine.add(maPoint);
        }

        return maLine;
    }

    private LocalDate calculateStartDate(PeriodType periodType) {
        LocalDate endDate = LocalDate.now();

        return switch (periodType) {
            case PeriodType.DAILY -> endDate.minusDays(1);
            case PeriodType.WEEKLY -> endDate.minusWeeks(1);
            case PeriodType.MONTHLY -> endDate.minusMonths(1);
            case PeriodType.QUARTERLY -> endDate.minusMonths(3);
            case PeriodType.YEARLY -> endDate.minusYears(1);
        };
    }

    private Optional<IndexPerformanceDto> createIndexPerformanceDto(IndexInfo
        indexInfo, Map<Integer, IndexData> beforeDataMap, Map<Integer, IndexData> currentDataMap) {
        IndexData startData = beforeDataMap.get(indexInfo.getId());
        IndexData endData = currentDataMap.get(indexInfo.getId());

        if (startData != null && endData != null) {
            double beforePrice = startData.getClosePrice();
            double currentPrice = endData.getClosePrice();
            double versus = currentPrice - beforePrice;
            double fluctuationRate = (versus / beforePrice) * 100.0;

            return Optional.of(new IndexPerformanceDto(
                indexInfo.getId(),
                indexInfo.getIndexClassification(),
                indexInfo.getIndexName(),
                versus,
                fluctuationRate,
                currentPrice,
                beforePrice
            ));
        }

        return Optional.empty();
    }
}
