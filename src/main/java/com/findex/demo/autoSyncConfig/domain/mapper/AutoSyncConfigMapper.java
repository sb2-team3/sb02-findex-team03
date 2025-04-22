package com.findex.demo.autoSyncConfig.domain.mapper;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoSyncConfigMapper {

//        @Mapping(target = "indexInfoId", source = "indexInfo.id")
//        @Mapping(target = "indexClassification", source = "indexInfo.indexClassification")
//        @Mapping(target = "indexName", source = "indexInfo.indexName")
        AutoSyncConfigDto toAutoSyncConfigUpdateResponse(AutoSyncConfig config, IndexInfo indexInfo);
}
