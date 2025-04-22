package com.findex.demo.indexData.index.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IndexDataService {

//  private final IndexInfoRepository indexInfoRepository;
//  private final IndexDataRepository indexDataRepository;
//
//  public void registerIndexData(IndexDataCreateRequest request) {
//
//    // 1. 해당 지수 정보 존재 여부 확인
//    IndexInfo indexInfo = indexInfoRepository.findById(request.getIndexInfoId())
//        .orElseThrow(() -> new IllegalArgumentException("지수 정보가 존재하지 않습니다."));
//
//    // 2. 중복 확인
//    boolean exists = indexDataRepository.existsByIndexInfoAndDate(indexInfo, request.getBaseDate());
//    if (exists) {
//      throw new CustomException(ErrorCode.VALIDATION_ERROR,"이미 해당 날짜에 지수 데이터가 등록되어 있습니다.");
//    }
//
//    // 3. 등록
//    IndexDataMapper mapper = new IndexDataMapper();
//    IndexData data = mapper.toEntity(request);
//    indexDataRepository.save(data);
//  }
}