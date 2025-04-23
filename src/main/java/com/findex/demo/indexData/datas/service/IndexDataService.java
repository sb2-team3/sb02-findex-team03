package com.findex.demo.indexData.datas.service;

import com.findex.demo.indexData.datas.domain.dto.DataPoint;
import com.findex.demo.indexData.datas.domain.dto.IndexChartDto;
import com.findex.demo.indexData.datas.domain.dto.IndexPerformanceDto;
import com.findex.demo.indexData.datas.domain.dto.Performance;
import com.findex.demo.indexData.datas.domain.dto.PeriodType;
import com.findex.demo.indexData.datas.domain.dto.RankedIndexPerformanceDto;
import com.findex.demo.indexData.datas.repository.IndexDataRepository;
import com.findex.demo.indexData.datas.repository.IndexInfoRepository;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class IndexDataService {

    IndexInfoRepository indexInfoRepository;
    IndexDataRepository indexDataRepository;

    @Transactional(readOnly = true)
    public IndexChartDto getIndexChart(PeriodType periodType, Integer indexInfoId) {
        LocalDate startDate = calculateStartDate(periodType);
        LocalDate endDate = LocalDate.now();

        // ID로 지수 정보 가져오기 .
        IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
            .orElseThrow(() -> new NoSuchElementException("[ERROR] index info not found"));

        // IndexInfo와 기준 일자로 데이터 뽑아오기
        List<IndexData> indexDataList = indexDataRepository
            .findByIndexInfoAndDateBetweenOrderByDateAsc(indexInfo, startDate, endDate);

        // indexData를 차트 데이터로 변경 (날짜 + 종가)
        List<DataPoint> dataPoints = indexDataList.stream()
            .map(indexData -> new DataPoint(indexData.getBaseDate(),
                indexData.getClosePrice().doubleValue())).toList();

        // 이동 평균선 만들기.
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
    public List<RankedIndexPerformanceDto> getIndexPerformanceRank(PeriodType periodType,
        Integer indexInfoId, int limit) {

        LocalDate startDate = calculateStartDate(periodType);
        LocalDate endDate = LocalDate.now();

        List<IndexInfo> indexInfoList;

        if (indexInfoId == null || indexInfoId <= 0) {
            indexInfoList = indexInfoRepository.findAll();  // 모든 IndexInfo 조회
        } else {
            IndexInfo targetIndexInfo = indexInfoRepository.findById(indexInfoId)
                .orElse(null);
            if (targetIndexInfo == null) {
                throw new NoSuchElementException("[ERROR] index info not found");
            }

            indexInfoList = indexInfoRepository.findByIndexClassification(
                targetIndexInfo.getIndexClassification());
        }

        List<IndexData> indexDataList = indexDataRepository.findByIndexInfoInAndDateBetween(
            indexInfoList, startDate, endDate);

        Map<Integer, IndexData> startDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(startDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

        Map<Integer, IndexData> endDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(endDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

        // 성과 계산 및 DTO 생성
        List<IndexPerformanceDto> performanceList = new ArrayList<>();

        for (IndexInfo indexInfo : indexInfoList) {
            Integer id = indexInfo.getId();

            // 시작일과 종료일 데이터가 모두 있는 경우에만 성과 계산
            if (startDateMap.containsKey(id) && endDateMap.containsKey(id)) {
                IndexData startData = startDateMap.get(id);
                IndexData endData = endDateMap.get(id);

                Double startPrice = startData.getClosePrice();
                Double endPrice = endData.getClosePrice();

                // 등락률 계산 (%)
                double fluctuationRate = 0;
                if (startPrice.compareTo(0.0) != 0) {
                    fluctuationRate = (endPrice - startPrice) / startPrice * 100;
                }

                // 등락폭 계산
                double versus = endPrice - startPrice;

                IndexPerformanceDto dto = new IndexPerformanceDto(
                    id,
                    indexInfo.getIndexClassification(),
                    indexInfo.getIndexName(),
                    versus,
                    fluctuationRate,
                    endPrice.doubleValue(),
                    startPrice.doubleValue()
                );

                performanceList.add(dto);
            }
        }

        // 등락률 기준으로 내림차순 정렬
        performanceList.sort(
            Comparator.comparingDouble(IndexPerformanceDto::getFluctuationRate).reversed());

        // 상위 limit개만 선택
        List<IndexPerformanceDto> topPerformances = performanceList.stream()
            .limit(limit)
            .collect(Collectors.toList());

        // RankedIndexPerformanceDto 생성 및 순위 부여
        List<RankedIndexPerformanceDto> result = new ArrayList<>();
        for (int i = 0; i < topPerformances.size(); i++) {
            IndexPerformanceDto perfDto = topPerformances.get(i);

            // Performance 객체 생성 (생성자 사용)
            Performance performance = new Performance(
                perfDto.getIndexInfoId(),
                perfDto.getIndexClassification(),
                perfDto.getIndexName(),
                perfDto.getVersus(),
                perfDto.getFluctuationRate(),
                perfDto.getCurrentPrice(),
                perfDto.getBeforePrice()
            );

            // RankedIndexPerformanceDto 생성 (생성자 사용)
            RankedIndexPerformanceDto rankedDto = new RankedIndexPerformanceDto(performance, i + 1);

            result.add(rankedDto);
        }

        return result;
    }

    // 관심 지수 성과 조회 ..
    @Transactional(readOnly = true)
    public List<IndexPerformanceDto> getInterestIndexPerformance(PeriodType periodType) {
        List<IndexInfo> favoriteIndexes = indexInfoRepository.findByFavoriteIsTrue();

        // 일간 or 주간 or 월간 성과
        LocalDate startDate = calculateStartDate(periodType);
        LocalDate endDate = LocalDate.now();

        // 관심 종목 ID 리스트 ..
        List<Integer> favoriteIndexIds = favoriteIndexes.stream()
            .map(IndexInfo::getId)
            .toList();

        // 관심 종목 ID로 indexData 가져오기 ..
        List<IndexData> indexDataList = new ArrayList<>();
        for (Integer favoriteIndexId : favoriteIndexIds) {
            IndexData data = indexDataRepository.findByIndexInfoIdAndDateBetween(
                favoriteIndexId, startDate, endDate);

            indexDataList.add(data);
        }

        Map<Integer, IndexData> startDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(startDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(),
                Function.identity()));

        Map<Integer, IndexData> endDateMap = indexDataList.stream()
            .filter(data -> data.getBaseDate().equals(endDate))
            .collect(Collectors.toMap(data -> data.getIndexInfo().getId(), Function.identity()));

        return favoriteIndexes.stream()
            .map(indexInfo -> createIndexPerformanceDto(indexInfo, startDateMap, endDateMap))
            .toList();
    }

    // 최근 5일의 주가 종가 더한 후 5로 나눈 값 .
    private List<DataPoint> calculateMovingAverage(List<DataPoint> dataPoints, int period) {
        List<DataPoint> maLine = new ArrayList<>();

        // 먄약에 데이터가 20개면 앞에 4개(index=0,1,2,3)은 이동 평균을 구할 수 없음. 과거 데이터가 없기 때문에.
        for (int cnt_i = period - 1; cnt_i < dataPoints.size(); cnt_i++) {
            double sum = 0;

            for (int cnt_j = 0; cnt_j < period; cnt_j++) {
                sum += dataPoints.get(cnt_i - cnt_j).getValue();
            }

            double average = sum / period;

            DataPoint maPoint = new DataPoint(
                dataPoints.get(cnt_i).getData(), average
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

    private IndexPerformanceDto createIndexPerformanceDto(IndexInfo
        indexInfo, Map<Integer, IndexData> beforeDataMap, Map<Integer, IndexData> currentDataMap) {
        IndexData startData = beforeDataMap.get(indexInfo.getId());
        IndexData endData = currentDataMap.get(indexInfo.getId());

        if (startData != null && endData != null) {
            double beforePrice = startData.getClosePrice().doubleValue();
            double currentPrice = endData.getClosePrice().doubleValue();
            double versus = currentPrice - beforePrice;
            double fluctuationRate = (versus / beforePrice) * 100.0;

            return new IndexPerformanceDto(
                indexInfo.getId(),
                indexInfo.getIndexClassification(),
                indexInfo.getIndexName(),
                versus,
                fluctuationRate,
                currentPrice,
                beforePrice
            );
        }

        return null;
    }
}
