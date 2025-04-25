package com.findex.demo.autoSyncConfig.controller;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.service.AutoSyncConfigService;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController()
@RequestMapping("/api/auto-sync-configs")
@RequiredArgsConstructor
public class AutoSyncConfigController {

    private final AutoSyncConfigService autoSyncConfigService;

    @PatchMapping("/{indexInfoId}")
    public ResponseEntity<AutoSyncConfigDto> updateAutoSyncConfig(@PathVariable Integer indexInfoId,@RequestBody AutoSyncConfigUpdateRequest request) {
        AutoSyncConfigDto autoSyncConfigDto = autoSyncConfigService.updateAutoSyncConfig(indexInfoId, request);
        return ResponseEntity.ok(autoSyncConfigDto);
    }

    @GetMapping
    public ResponseEntity<PagedResponse<AutoSyncConfig>> pageAutoSyncConfigs(
            @RequestParam(required = false) Integer indexInfoId,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(defaultValue = "0") int idAfter,
            @RequestParam(required = false) String cursor,
            @RequestParam(defaultValue = "indexInfo.indexName") String sortField,
            @RequestParam(defaultValue = "asc") String sortDirection,
            @RequestParam(defaultValue = "10") int size) {

        SortField resolved = SortField.from(sortField);
        SortDirection resolvedDirection = SortDirection.from(sortDirection);
        PagedResponse<AutoSyncConfig> page = autoSyncConfigService.getPageAutoSynConfig(
                indexInfoId, enabled, idAfter, cursor, resolved, resolvedDirection, size);

        return ResponseEntity.ok(page);
    }

}
