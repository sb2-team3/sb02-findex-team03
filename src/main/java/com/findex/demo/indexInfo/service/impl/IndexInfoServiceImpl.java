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
  public CursorPageResponseIndexInfoDto getIndexInfoList(String indexClassification,
      String indexName, Boolean favorite, Long idAfter, String cursor, String sortField,
      String sortDirection, int size) {

    return null;
  }


  public IndexInfo toEntity(IndexInfoCreateRequest createRequest) {
    if ( createRequest == null ) {
      return null;
    }

    IndexInfo.IndexInfoBuilder indexInfo = IndexInfo.builder();

    indexInfo.employedItemCount( createRequest.employedItemsCount() );
    indexInfo.indexClassification( createRequest.indexClassification() );
    indexInfo.indexName( createRequest.indexName() );
    indexInfo.basePointInTime( createRequest.basePointInTime() );
    indexInfo.baseIndex( createRequest.baseIndex() );
    indexInfo.favorite( createRequest.favorite() );

    indexInfo.sourceType( SourceType.USER );

    return indexInfo.build();
  }


  public IndexInfoDto toDto(IndexInfo indexInfo) {
    if ( indexInfo == null ) {
      return null;
    }

    IndexInfoDto.IndexInfoDtoBuilder indexInfoDto = IndexInfoDto.builder();

    indexInfoDto.id( indexInfo.getId() );
    indexInfoDto.indexClassification( indexInfo.getIndexClassification() );
    indexInfoDto.indexName( indexInfo.getIndexName() );
    indexInfoDto.basePointInTime( indexInfo.getBasePointInTime() );
    indexInfoDto.baseIndex( indexInfo.getBaseIndex() );
    indexInfoDto.sourceType( indexInfo.getSourceType() );
    indexInfoDto.favorite( indexInfo.isFavorite() );

    return indexInfoDto.build();
  }


  public IndexInfoSummaryDto toSummaryDto(IndexInfo indexInfo) {
    if ( indexInfo == null ) {
      return null;
    }

    Integer id = null;
    String indexClassification = null;
    String indexName = null;

    id = indexInfo.getId();
    indexClassification = indexInfo.getIndexClassification();
    indexName = indexInfo.getIndexName();

    IndexInfoSummaryDto indexInfoSummaryDto = new IndexInfoSummaryDto( id, indexClassification, indexName );

    return indexInfoSummaryDto;
  }


  public void updateFromDto(IndexInfoUpdateRequest request, IndexInfo indexInfo) {
    if ( request == null ) {
      return;
    }

    indexInfo.setBasePointInTime( request.basePointInTime() );
    indexInfo.setBaseIndex( request.baseIndex() );
    indexInfo.setFavorite( request.favorite() );
  }
}
