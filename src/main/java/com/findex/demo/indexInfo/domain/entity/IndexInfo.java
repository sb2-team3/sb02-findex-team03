package com.findex.demo.indexInfo.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.Instant;
import java.time.LocalDate;
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
<<<<<<< HEAD
  private LocalDateTime basePointInTime;
=======
  private LocalDate basePointInTime;
>>>>>>> 357bad6 (IndexInfo 임시 완성본)
  private double baseIndex;

  @Enumerated(EnumType.STRING)
  private SourceType sourceType;

  private boolean favorite = false;

//  public void setEmployedItemCount(int employedItemCount) {
//    this.employedItemCount = employedItemCount;
//  }
//
//  public void setSourceType(SourceType sourceType) {
//    this.sourceType = sourceType;
//  }
}
