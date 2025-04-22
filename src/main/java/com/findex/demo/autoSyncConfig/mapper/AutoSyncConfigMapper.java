package com.findex.demo.autoSyncConfig.mapper;

import com.findex.demo.autoSyncConfig.domain.dto.AutoSyncConfigDto;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AutoSyncConfigMapper {

  @Mapping(source = "config.id", target = "id")
  @Mapping(source = "indexInfo.id", target = "indexInfoId")
  AutoSyncConfigDto toAutoSyncConfigUpdateResponse(AutoSyncConfig config, IndexInfo indexInfo);
}
