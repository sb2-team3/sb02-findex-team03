package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoSyncConfigRepository extends JpaRepository<AutoSyncConfig, Integer> ,
        AutoSyncConfigRepositoryCustom {

    Optional<AutoSyncConfig> findByIndexInfo_Id(Integer indexInfoId);

    boolean existsByIndexInfoId(Integer indexInfoId);

    void deleteByIndexInfo(IndexInfo indexInfo);
}
