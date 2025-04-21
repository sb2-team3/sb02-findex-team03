package com.findex.demo.indexData.index.repository;

import com.findex.demo.indexData.index.domain.entity.IndexData;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexDataRepository extends JpaRepository<UUID, IndexData> {

}
