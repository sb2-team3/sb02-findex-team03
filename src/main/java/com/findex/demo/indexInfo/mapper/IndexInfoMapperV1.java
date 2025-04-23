package com.findex.demo.indexInfo.mapper;

import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;

public class IndexInfoMapperV1 {

    /**
     * TODO : mapper class 예시
     *     3 번 IndexInfoService create 확인 해보세요
     */
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
}
