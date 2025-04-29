package com.findex.demo.syncJobs.api;


import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.domain.dto.IndexDataSyncRequestDto;
import com.findex.demo.syncJobs.domain.dto.SyncJobDto;
import com.findex.demo.syncJobs.domain.entity.SyncJob;
import com.findex.demo.syncJobs.domain.type.JobType;
import com.findex.demo.syncJobs.domain.type.StatusType;
import com.findex.demo.syncJobs.mapper.SyncJobMapper;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class MarketIndexSyncController {

  private final MarketIndexSyncService marketIndexSyncService;
  private final MarketIndexDataSyncService marketIndexDataSyncService;
  private final IndexInfoRepository indexInfoRepository;
  private final SyncJobRepository syncJobRepository;

  @PostMapping("/index-infos")
  public ResponseEntity<List<SyncJobDto>> syncMarketIndexInfo() {
    List<SyncJobDto> syncJobsAndConfigs = marketIndexSyncService.createSyncJobsAndConfigs();
    return ResponseEntity.ok(syncJobsAndConfigs);
  }

  @PostMapping("/index-data")
  public ResponseEntity<List<SyncJobDto>> syncIndexData(@RequestBody IndexDataSyncRequestDto request) {
    try {
      List<IndexInfo> indexInfos = indexInfoRepository.findAllById(request.getIndexInfoIds());

      List<String> indexNames = indexInfos.stream()
          .map(IndexInfo::getIndexName)
          .collect(Collectors.toList());

      List<SyncJob> results = new ArrayList<>();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

      for (String date : dateList(request.getBaseDateFrom(), request.getBaseDateTo())) {
        marketIndexDataSyncService.fetchIndexData(date, indexNames);
        for (IndexInfo info : indexInfos) {
          results.add(SyncJob.builder()
              .worker(InetAddress.getLocalHost().getHostAddress())
              .id(null)
              .jobType(JobType.INDEX_DATA)
              .indexInfo(indexInfoRepository.findById(info.getId()).orElseThrow())
              .targetDate(LocalDate.parse(date, formatter))
              .jobTime(LocalDate.now())
              .statusType(StatusType.SUCCESS)
              .build());
        }
      }

      List<SyncJob> savedResults = syncJobRepository.saveAll(results);

      List<SyncJobDto> dtoList = savedResults.stream()
          .map(SyncJobMapper::toSyncJobDto)
          .collect(Collectors.toList());

      return ResponseEntity.ok(dtoList);

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  private List<String> dateList(String from, String to) {
    LocalDate start = LocalDate.parse(from);
    LocalDate end = LocalDate.parse(to);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    return start.datesUntil(end.plusDays(1))
        .map(date -> date.format(formatter))
        .collect(Collectors.toList());
  }
}
