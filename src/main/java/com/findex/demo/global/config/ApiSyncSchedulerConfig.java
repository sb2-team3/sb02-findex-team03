package com.findex.demo.global.config;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.repository.AutoSyncConfigRepository;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.syncJobs.api.MarketIndexDataSyncService;
import com.findex.demo.syncJobs.repository.SyncJobRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSyncSchedulerConfig {

    private final MarketIndexDataSyncService marketIndexDataSyncService;
    private final IndexInfoRepository indexInfoRepository;
    private final AutoSyncConfigRepository autoSyncConfigRepository;
    private final SyncJobRepository syncJobRepository;

    @Scheduled(cron = "0 42 10 * * *", zone = "Asia/Seoul")
    public void scheduleAutoSyncIndexData() {
        log.info("⏰ [스케줄러] 지수 데이터 자동 연동 시작");

        LocalDate today = LocalDate.now();
        List<AutoSyncConfig> activeConfigs = autoSyncConfigRepository.findAllByEnabledTrue();

        for (AutoSyncConfig config : activeConfigs) {
            Integer indexInfoId = config.getIndexInfo().getId();

            // IndexInfo 찾아오기
            IndexInfo indexInfo = indexInfoRepository.findById(indexInfoId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 IndexInfo ID: " + indexInfoId));

            LocalDate lastSyncedDate = syncJobRepository.findLastSuccessSyncTime(indexInfo.getId());
            if (lastSyncedDate == null) {
                lastSyncedDate = indexInfo.getBasePointInTime(); // 기준일로 초기화
            }
            LocalDate targetDate = lastSyncedDate.plusDays(1);
            while (!targetDate.isAfter(today)) {
                String targetDateString = targetDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                marketIndexDataSyncService.fetchIndexData(targetDateString, List.of(indexInfo.getIndexName()));
                targetDate = targetDate.plusDays(1);
            }
        }

        log.info("⏰ [스케줄러] 지수 데이터 자동 연동 완료");
    }
}
