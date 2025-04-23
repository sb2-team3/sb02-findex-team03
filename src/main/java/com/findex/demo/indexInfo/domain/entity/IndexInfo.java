package com.findex.demo.indexInfo.domain.entity;

import com.findex.demo.syncJobs.api.ExternalIndexInfoDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Setter
@NoArgsConstructor
public class IndexInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String indexClassification;
  private String indexName;
  private int employedItemCount;
  private LocalDate basePointInTime;
  private int baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private boolean favorite = false;

  /*
  TODO: 1번 생성자와 함께 @Builder 애노테이션을 적용하여 빌더 패턴으로 생성할 수 있도록 합니다.
       2번 IndexInfoMapperV1 에서  toIndexInfoDto 구현된 메소드 확인
 */

  @Builder
  public IndexInfo(String indexClassification, String indexName, int employedItemCount, LocalDate basePointInTime,
                   Integer baseIndex, SourceType sourceType, boolean favorite) {
    this.indexClassification = indexClassification;
    this.indexName = indexName;
    this.employedItemCount = employedItemCount;
    this.basePointInTime = basePointInTime;
    this.baseIndex = baseIndex;
    this.sourceType = sourceType;
    this.favorite = favorite;
  }

  public void updateFromDto(ExternalIndexInfoDto dto) {
    this.baseIndex = dto.basId();
    this.employedItemCount = dto.employedItemCount();
    this.basePointInTime = LocalDate.parse(dto.basePointInTimeRaw(), DateTimeFormatter.ofPattern("yyyyMMdd"));
  }

}
