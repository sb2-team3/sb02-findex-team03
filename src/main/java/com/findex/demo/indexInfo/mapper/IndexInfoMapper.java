package com.findex.demo.indexInfo.mapper;

import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IndexInfoMapper {
  IndexInfo toEntity(IndexInfoCreateRequest request);
  IndexInfoDto toDto(IndexInfo indexInfo);
  IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo);
  void updateFromDto(IndexInfoUpdateRequest request, @MappingTarget IndexInfo indexInfo);
}