package com.findex.demo.autoSyncConfig.controller;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;
import com.findex.demo.autoSyncConfig.service.AutoSyncConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/auto-sync-configs")
//@RequiredArgsConstructor
public class AutoSyncConfigController {

    private final AutoSyncConfigService autoSyncConfigService;

    public AutoSyncConfigController(AutoSyncConfigService autoSyncConfigService) {
        this.autoSyncConfigService = autoSyncConfigService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AutoSyncConfigDto> updateAutoSyncConfig(@PathVariable Integer id, AutoSyncConfigUpdateRequest request) {
        AutoSyncConfigDto autoSyncConfigDto = autoSyncConfigService.updateAutoSyncConfig(id, request);
        return ResponseEntity.ok(autoSyncConfigDto);
    }

}
