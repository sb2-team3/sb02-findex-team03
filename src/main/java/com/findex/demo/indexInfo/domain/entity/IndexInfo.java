package com.findex.demo.indexInfo.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.indexInfo.domain.dto.IndexInfoUpdateRequest;
import com.findex.demo.syncJobs.api.ExternalIndexInfoDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Entity
@Table(name = "index_info")
@NoArgsConstructor
public class IndexInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false)
  private String indexClassification;

  @Column(name = "index_name", nullable = false)
  private String indexName;

  @Column(nullable = false)
  @Positive
  private int employedItemCount;

  @Column(nullable = false)
  private LocalDate basePointInTime;

  @Column(nullable = false)
  @Positive
  private int baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private Boolean favorite = false;

  @Builder
  public IndexInfo(String indexClassification, String indexName, int employedItemCount, LocalDate basePointInTime,
      Integer baseIndex, SourceType sourceType, Boolean favorite) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.employedItemCount = employedItemCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.sourceType = sourceType;
    this.favorite = (favorite != null) ? favorite : false;
  }

  public void update(IndexInfoUpdateRequest updateRequest) {
    if (updateRequest.employedItemsCount() != 0 && updateRequest.employedItemsCount() != this.employedItemCount) {
      if (updateRequest.employedItemsCount() < 0) {
        throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
      }
      this.employedItemCount = updateRequest.employedItemsCount();
    }
    if (updateRequest.basePointInTime() != null && !updateRequest.basePointInTime().equals(this.basePointInTime)) {
      this.basePointInTime = updateRequest.basePointInTime();
    }
    if (updateRequest.baseIndex() != 0 && updateRequest.baseIndex() != this.baseIndex) {
      if (updateRequest.baseIndex() < 0) {
        throw new CustomException(ErrorCode.INVALID_INPUT_VALUE, "부서 코드는 필수입니다");
      }
      this.baseIndex = updateRequest.baseIndex();
    }
    if (updateRequest.favorite() != null && !updateRequest.favorite().equals(this.favorite)) {
      this.favorite = updateRequest.favorite();
    } else if (updateRequest.favorite() == null && this.favorite == null) {
      this.favorite = false;
    }
  }

  public void updateFromDto(ExternalIndexInfoDto dto) {
    this.baseIndex = dto.basId();
    this.employedItemCount = dto.employedItemCount();
    this.basePointInTime = LocalDate.parse(dto.basePointInTimeRaw(), DateTimeFormatter.ofPattern("yyyyMMdd"));
  }
}
