package com.findex.demo.global.config;

import com.findex.demo.syncJobs.api.MarketIndexDataSyncService;
import com.findex.demo.syncJobs.api.MarketIndexSyncService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiSyncSchedulerConfig {

    private final MarketIndexDataSyncService marketIndexDataSyncService;
    private final MarketIndexSyncService marketIndexSyncService;

    @Scheduled(cron = "0 49 3 * * *")
    public void scheduleFetchIndexData() {
        log.info("⏰ [스케줄러] 지수 데이터 수집 시작");

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        marketIndexDataSyncService.fetchIndexData(today, new ArrayList<>());

        marketIndexSyncService.createSyncJobsAndConfigs();

        log.info("⏰ [스케줄러] 지수 데이터 수집 완료");
    }
}
