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
  private final IndexInfoMapper indexInfoMapper;

  @Override
  @Transactional
  public IndexInfoDto createIndexInfo(IndexInfoCreateRequest createRequest) {
    String indexClassification = createRequest.indexClassification();
    String indexName = createRequest.indexName();

    if (indexInfoRepository.existsByIndexClassificationAndIndexName(indexClassification, indexName)) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "지수 분류명과 지수명은 중복될 수 없습니다.");
    }

    IndexInfo indexInfo = indexInfoMapper.toEntity(createRequest);

    indexInfo.setSourceType(SourceType.USER);

    indexInfo = indexInfoRepository.save(indexInfo);

    return indexInfoMapper.toDto(indexInfo);
  }


  @Override
  @Transactional
  public IndexInfoDto updateIndexInfo(Integer id, IndexInfoUpdateRequest updateRequest) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("지수 정보가 존재하지 않습니다."));

    indexInfoMapper.updateFromDto(updateRequest, indexInfo);

    indexInfoRepository.save(indexInfo);

    return indexInfoMapper.toDto(indexInfo);
  }

  @Override
  @Transactional
  public void deleteIndexInfo(Integer id) {
    IndexInfo indexInfo = indexInfoRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("지수 정보가 존재하지 않습니다."));

    indexInfoRepository.delete(indexInfo);
  }

  @Override
  public IndexInfoDto getIndexInfo(Integer id) {
    return indexInfoRepository.findById(id)
        .map(indexInfoMapper::toDto)
        .orElseThrow(() -> new IllegalArgumentException("지수 정보가 존재하지 않습니다."));
  }

  @Override
  public List<IndexInfoSummaryDto> getIndexInfoSummaries() {
    List<IndexInfo> indexInfos = indexInfoRepository.findAll();

    return indexInfos.stream()
        .map(indexInfoMapper::toSummaryDto)
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
