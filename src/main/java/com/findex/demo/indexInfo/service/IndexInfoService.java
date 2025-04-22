package com.findex.demo.indexInfo.service;

import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import java.util.List;

public interface IndexInfoService {

    IndexInfoDto createIndexInfo(IndexInfoCreateRequest createRequest);

    IndexInfoDto updateIndexInfo(Integer id, IndexInfoUpdateRequest updateRequest);

    void deleteIndexInfo(Integer id);

    IndexInfoDto getIndexInfo(Integer id);

    List<IndexInfoSummaryDto> getIndexInfoSummaries();

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
}