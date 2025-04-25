package com.findex.demo.autoSyncConfig.mapper;


import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;


public class AutoSyncConfigMapper {

    public static AutoSyncConfigDto toAutoSyncConfigDto(AutoSyncConfig autoSyncConfig) {
        return new AutoSyncConfigDto(autoSyncConfig.getId(), autoSyncConfig.getIndexInfo().getId(), autoSyncConfig.getIndexInfo().getIndexClassification(), autoSyncConfig.getIndexInfo().getIndexName(), autoSyncConfig.getEnabled());
    }

    public static AutoSyncConfig toAutoSyncConfig(Boolean enabled, IndexInfo indexInfo) {
        return AutoSyncConfig.builder()
                .enabled(enabled)
                .indexInfo(indexInfo)
                .build();
    }



}
