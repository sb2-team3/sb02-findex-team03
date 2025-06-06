package com.findex.demo.indexData.index.controller;

import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataCreateRequest;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.dto.IndexDataUpdateRequest;
import com.findex.demo.indexData.index.domain.entity.IndexData;
//import com.findex.demo.indexData.index.service.CSVExportIndexDataService;
import com.findex.demo.indexData.index.service.CSVExportIndexDataService;
import com.findex.demo.indexData.index.service.IndexDataService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/index-data")
public class IndexDataController {

  private final IndexDataService indexDataService;
  private final CSVExportIndexDataService csvExportIndexDataService;
  /*
  TODO : 목요일 에  같이 구현
   */
  /**
   * 지수 데이터 목록조회
   */
  @GetMapping
  public ResponseEntity<CursorPageResponseIndexDataDto<IndexDataDto>> findAll(
      @RequestParam(defaultValue = "1") Integer indexInfoId,
      @RequestParam(defaultValue = "2010-01-01") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      @RequestParam(defaultValue = "eyJpZCI6MjB9") String cursor,
      @RequestParam(defaultValue = "2") Integer idAfter,
      @RequestParam(defaultValue = "baseDate") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection,
      @RequestParam(defaultValue = "10") Integer size
  ) {
    if (endDate == null) {
      // 현재 날짜를 yyyy-MM-dd 포맷으로 변환
      LocalDate today = LocalDate.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      endDate = LocalDate.parse(today.format(formatter)) ;
    }
    IndexDataSearchCondition request = new IndexDataSearchCondition(
        indexInfoId, startDate, endDate, idAfter, cursor,
        sortField, sortDirection, size
    );


      CursorPageResponseIndexDataDto<IndexDataDto> dto = indexDataService.findAll(request);



    return ResponseEntity.status(HttpStatus.OK).body(dto);
  }
  /**

  /**
   * 지수 데이터 생성
   */
  @PostMapping
  public ResponseEntity<IndexDataDto> create(@RequestBody @Valid IndexDataCreateRequest request) {
    IndexDataDto created = indexDataService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }
  /**
   * 지수 데이터 수정
   */
  @PatchMapping("/{id}")
  public ResponseEntity<IndexDataDto> update(
      @PathVariable Integer id,
      @RequestBody @Valid IndexDataUpdateRequest request) {
    IndexDataDto updated = indexDataService.update(id, request);
    return ResponseEntity.ok(updated);
  }

  /**
   * 지수 데이터 삭제
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Integer id) {
    indexDataService.delete(id);
    return ResponseEntity.noContent().build(); // HTTP 204
  }

  /**
   * csv export
   */
  @GetMapping("/export/csv")
  public ResponseEntity<byte[]> downloadCsvFile() throws Exception{

    byte[] response = csvExportIndexDataService.downloadCsv();

    if(response == null) {
      return ResponseEntity.internalServerError().build();
    }

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");
    return new ResponseEntity<>(response, headers, HttpStatus.OK);
  }
}


