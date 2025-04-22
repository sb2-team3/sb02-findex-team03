package com.findex.demo.autoSyncConfig.service;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigUpdateRequest;

public interface AutoSyncConfigService {

    AutoSyncConfigDto updateAutoSyncConfig(Integer autoSyConfigId, AutoSyncConfigUpdateRequest request);
}
