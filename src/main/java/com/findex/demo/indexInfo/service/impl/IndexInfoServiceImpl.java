package com.findex.demo.indexInfo.service.impl;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.dto.CursorPageResponseIndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.mapper.IndexInfoMapper;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.indexInfo.service.IndexInfoService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexInfoServiceImpl implements IndexInfoService {

  private final IndexInfoRepository indexInfoRepository;

  @Override
  @Transactional
  public IndexInfoDto create(IndexInfoCreateRequest createRequest) {
    String indexClassification = createRequest.indexClassification();
    String indexName = createRequest.indexName();

    if (indexClassification == null || indexClassification.trim().isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
    }
    if (indexName == null || indexName.trim().isEmpty()) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
    }

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
    }

    IndexInfo indexInfo = IndexInfoMapper.toEntity(createRequest, SourceType.USER);

    indexInfo = indexInfoRepository.save(indexInfo);

    return IndexInfoMapper.toIndexInfoDto(indexInfo);
  }

  @Override
  @Transactional
  public IndexInfoDto update(Integer id, IndexInfoUpdateRequest updateRequest) {
    IndexInfo existingIndexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));

    existingIndexInfo.update(updateRequest);

    IndexInfo updated = indexInfoRepository.save(existingIndexInfo);

    return IndexInfoMapper.toIndexInfoDto(updated);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));

    indexInfoRepository.delete(indexInfo);
  }

  @Override
  @Transactional(readOnly = true)
  public IndexInfoDto getIndexInfo(Integer id) {
    return indexInfoRepository.findById(id)
        .map(IndexInfoMapper::toIndexInfoDto)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다"));
  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();

    return indexInfos.stream()
        .map(IndexInfoMapper::toSummaryDto)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseIndexInfoDto getIndexInfoList(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      String sortDirection,
      int size) {

    boolean isAscending = "asc".equalsIgnoreCase(sortDirection);

    Long cursorValue = (idAfter != null) ? idAfter : (cursor != null ? Long.parseLong(cursor) : 0L);

    Pageable pageable = PageRequest.of(0, size, Sort.by(
        isAscending ? Sort.Order.asc(sortField) : Sort.Order.desc(sortField)
    ));

    Page<IndexInfo> pageResult = indexInfoRepository.findByFilter(
        indexClassification, indexName, favorite, cursorValue, pageable
    );

    List<IndexInfoDto> indexInfoDtos = pageResult.getContent().stream()
        .map(IndexInfoMapper::toIndexInfoDto)
        .toList();

    boolean hasNext = pageResult.hasNext();

    Integer nextCursor = null;
    if (!indexInfoDtos.isEmpty()) {
      IndexInfoDto lastItem = indexInfoDtos.get(indexInfoDtos.size() - 1);
      nextCursor = lastItem.id();
    }

    Long totalElements = indexInfoRepository.countByFilter(
        indexClassification, indexName, favorite, null
    );

    return new CursorPageResponseIndexInfoDto(
        new ArrayList<>(indexInfoDtos),
        nextCursor,
        nextCursor != null ? nextCursor.longValue() : null,
        size,
        totalElements,
        hasNext
    );
  }
}