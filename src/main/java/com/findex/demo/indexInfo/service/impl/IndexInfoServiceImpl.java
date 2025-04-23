package com.findex.demo.indexInfo.service.impl;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.dto.IndexInfoCreateRequest;
import com.findex.demo.indexInfo.domain.dto.IndexInfoDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoSummaryDto;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.mapper.IndexInfoMapper;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import com.findex.demo.indexInfo.service.IndexInfoService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수 입니다");
    }

    IndexInfo indexInfo = IndexInfoMapper.toEntity(createRequest, SourceType.USER);

    indexInfo = indexInfoRepository.save(indexInfo);

    return IndexInfoMapper.toIndexInfoDto(indexInfo);
  }

  @Override
  @Transactional
  public IndexInfoDto update(Integer id, IndexInfoUpdateRequest updateRequest) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수 입니다"));

    IndexInfoMapper.updateFromDto(updateRequest, indexInfo);

    indexInfoRepository.save(indexInfo);

    return IndexInfoMapper.toIndexInfoDto(indexInfo);
  }

  @Override
  @Transactional
  public void delete(Integer id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수 입니다"));

    indexInfoRepository.delete(indexInfo);
  }

  @Override
  public IndexInfoDto getIndexInfo(Integer id) {
    return indexInfoRepository.findById(id)
        .map(IndexInfoMapper::toIndexInfoDto)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수 입니다"));
  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();

    return indexInfos.stream()
        .map(IndexInfoMapper::toSummaryDto)
        .collect(Collectors.toList());
  }

  @Override
  public List<IndexInfoDto> getIndexInfoList(
      String indexClassification,
      String indexName,
      Boolean favorite,
      Long idAfter,
      String cursor,
      String sortField,
      String sortDirection,
      int size
  ) {
    return List.of();
  }
}
