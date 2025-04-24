package com.findex.demo.indexInfo.mapper;

import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;

public class IndexInfoMapper {

  public static IndexInfoDto toIndexInfoDto(IndexInfo indexInfo) {
    return IndexInfoDto.builder()
        .id(indexInfo.getId())
        .indexClassification(indexInfo.getIndexClassification())
        .indexName(indexInfo.getIndexName())
        .employedItemsCount(indexInfo.getEmployedItemCount())
        .basePointInTime(indexInfo.getBasePointInTime())
        .baseIndex(indexInfo.getBaseIndex())
        .sourceType(indexInfo.getSourceType())
        .favorite(indexInfo.isFavorite())
        .build();
  }

  public static IndexInfo toEntity(IndexInfoCreateRequest createRequest, SourceType sourceType) {
    return IndexInfo.builder()
        .indexClassification(createRequest.indexClassification())
        .indexName(createRequest.indexName())
        .employedItemCount(createRequest.employedItemsCount())
        .basePointInTime(createRequest.basePointInTime())
        .baseIndex(createRequest.baseIndex())
        .sourceType(sourceType)
        .favorite(createRequest.favorite())
        .build();
  }

  public static IndexInfo updateFromDto(IndexInfoUpdateRequest updateRequest, IndexInfo existingIndexInfo) {
    return existingIndexInfo.toBuilder()
        .employedItemCount(updateRequest.employedItemsCount() != 0 ? updateRequest.employedItemsCount() : existingIndexInfo.getEmployedItemCount())
        .basePointInTime(updateRequest.basePointInTime() != null ? updateRequest.basePointInTime() : existingIndexInfo.getBasePointInTime())
        .baseIndex(updateRequest.baseIndex() != 0 ? updateRequest.baseIndex() : existingIndexInfo.getBaseIndex())
        .favorite(updateRequest.favorite() != existingIndexInfo.isFavorite() ? updateRequest.favorite() : existingIndexInfo.isFavorite())
        .build();
  }

  public static IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo) {
    return IndexInfoSummaryDto.builder()
        .id(indexInfo.getId())
        .indexClassification(indexInfo.getIndexClassification())
        .indexName(indexInfo.getIndexName())
        .build();
  }
}
