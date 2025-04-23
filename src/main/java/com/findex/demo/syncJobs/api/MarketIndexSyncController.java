package com.findex.demo.syncJobs.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sync-jobs")
@RequiredArgsConstructor
public class MarketIndexSyncController {

    private final MarketIndexSyncService marketIndexSyncService;

    @PostMapping("/index-infos")
    public ResponseEntity<Void> syncMarketIndexInfo() {
        marketIndexSyncService.fetchAndStoreMarketIndices();
        return ResponseEntity.ok().build();
    }
}