package com.findex.demo.indexData.index.mapper;

import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IndexDataUpdateRequestMapper {

  @Mapping(target = "indexInfo", source = "indexInfo")
  @Mapping(target = "sourceType", expression = "java(SourceType.USER)")
  public IndexData toEntity(IndexDataCreateRequest dto, IndexInfo indexInfo);
}
