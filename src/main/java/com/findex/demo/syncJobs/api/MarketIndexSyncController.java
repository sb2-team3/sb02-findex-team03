package com.findex.demo.syncJobs.api;


import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.domain.dto.IndexDataSyncRequestDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.type.JobType;
import com.findex.demo.syncJobs.type.Result;
import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class MarketIndexSyncController {

    private final MarketIndexSyncService marketIndexSyncService;
    private final MarketIndexDataSyncService marketIndexDataSyncService;
    private final IndexInfoRepository indexInfoRepository;


    @PostMapping("/index-infos")
    public ResponseEntity<Void> syncMarketIndexInfo() {
        marketIndexSyncService.fetchAndStoreMarketIndices();
        return ResponseEntity.ok().build();
    }

    //indexdata 연동구현 ref: 이준혁
    @PostMapping("/index-data")
    public ResponseEntity<List<SyncJob>> syncIndexData(@RequestBody IndexDataSyncRequestDto request) {
        try {
            List<IndexInfo> indexInfos = indexInfoRepository.findAllById(request.getIndexInfoIds());

            List<String> indexNames = indexInfos.stream()
                .map(IndexInfo::getIndexName)
                .collect(Collectors.toList());

            List<String> dateList = getDateRange(request.getBaseDateFrom(), request.getBaseDateTo());

            List<SyncJob> results = new ArrayList<>();

            for (String date : dateList) {
                marketIndexDataSyncService.fetchIndexData(date, indexNames);
                for (IndexInfo info : indexInfos) {
                    results.add(SyncJob.builder()
                            .worker(InetAddress.getLocalHost().getHostAddress())
                        .id(null) // 저장 시 자동생성
                        .jobType(JobType.INDEX_DATA)
                        .indexInfo(indexInfoRepository.findById(info.getId()).orElseThrow() )
                        .targetDate(LocalDate.parse( request.getBaseDateFrom() ))

                        .jobTime(LocalDateTime.now())
                        .result(Result.SUCCESS)
                        .build());
                }
            }

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(results);

        } catch (Exception e) {
            log.error("❌ 연동 실패: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // 날짜 범위 리스트 생성 (예: 2023-01-01 ~ 2023-01-03 → ["20230101", "20230102", "20230103"])
    private List<String> getDateRange(String from, String to) {
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        return start.datesUntil(end.plusDays(1))
            .map(date -> date.format(formatter))
            .collect(Collectors.toList());
    }


}