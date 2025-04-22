package com.findex.demo.indexData.datas.repository;

import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndexInfoRepository extends JpaRepository<IndexInfo, Integer> {

    List<IndexInfo> findByFavoriteIsTrue();

    List<IndexInfo> findByIndexClassification(String indexClassification);
}
