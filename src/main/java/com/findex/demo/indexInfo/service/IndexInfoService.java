package com.findex.demo.indexInfo.service;

import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import java.util.List;

public interface IndexInfoService {

  List<IndexInfoDto> getIndexInfoList(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      String sortDirection,
      int size
  );

  IndexInfoDto createIndexInfo(IndexInfoCreateRequest createRequest);

  IndexInfoDto getIndexInfo(Long id);

  void deleteIndexInfo(Long id);

  IndexInfoDto updateIndexInfo(Long id, IndexInfoUpdateRequest updateRequest);

  List<IndexInfoSummaryDto> getIndexInfoSummaries();
}