package com.findex.demo.indexData.index.service;

import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexData.index.mapper.IndexDataDtoMapper;
import com.findex.demo.indexData.index.mapper.IndexDataUpdateRequestMapper;
import com.findex.demo.indexData.index.repository.IndexDataRepository;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataService {

  private final IndexInfoRepository indexInfoRepository;
  private final IndexDataRepository indexDataRepository;

  public IndexDataDto create(IndexDataCreateRequest request) {

    // 1. 해당 지수 정보 존재 여부 확인
    IndexInfo indexInfo = indexInfoRepository.findById(request.getIndexInfoId())
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다."));

    // 2. 중복 확인
    boolean exists = indexDataRepository.existsByIndexInfoAndDate(indexInfo, request.getBaseDate());
    if (exists) {
      throw new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다.");
    }

    // 3. 등록
    IndexDataUpdateRequestMapper mapper = new IndexDataUpdateRequestMapper();
    IndexData data = mapper.toEntity(request);
    indexDataRepository.save(data);

    IndexDataDtoMapper dtoMapper = new IndexDataDtoMapper();

   ;

    return  dtoMapper.toDto(data);
  }

  public IndexDataDto update(Integer id, IndexDataUpdateRequest request) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서 코드는 필수 입니다"));

    indexData.setOpenPrice(request.getMarketPrice());
    indexData.setClosePrice(request.getClosingPrice());
    indexData.setHighPrice(request.getHighPrice());
    indexData.setLowPrice(request.getLowPrice());
    indexData.setVersus(request.getVersus());
    indexData.setFluationRate(request.getFluctuationRate());
    indexData.setTradingQuantity(request.getTradingQuantity());
    indexData.setTradingPrice(request.getTradingPrice());
    indexData.setMarketTotalAmount(request.getMarketTotalAmount());

    IndexData updated = indexDataRepository.save(indexData);
    IndexDataDtoMapper mapper = new IndexDataDtoMapper();
    return mapper.toDto(updated);
  }

  /**
   * 지수 데이터 삭제
   * @param id IndexData ID
   */
  public void delete(Integer id) {
    IndexData indexData = indexDataRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_INPUT_VALUE,"부서코드는 필수 입니다."));
    indexDataRepository.delete(indexData);
  }
}