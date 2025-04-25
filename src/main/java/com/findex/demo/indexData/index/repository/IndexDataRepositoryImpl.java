package com.findex.demo.indexData.index.repository;



import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.IndexInfo;
import com.findex.demo.indexInfo.domain.entity.SourceType;
import com.findex.demo.indexInfo.repository.IndexInfoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class IndexDataRepositoryImpl implements IndexDataRepositoryCustom {

  @PersistenceContext
  private EntityManager em;


  @Override
  public List<IndexData> findByCondition(IndexDataSearchCondition condition, Pageable pageable) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<IndexData> cq = cb.createQuery(IndexData.class);
    Root<IndexData> root = cq.from(IndexData.class);


    List<Predicate> predicates = new ArrayList<>();

    if (condition.getIndexInfoId() != null) {
      predicates.add(cb.equal(root.get("indexInfo").get("id"), condition.getIndexInfoId()));
    }

    if (condition.getStartDate() != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("date"), condition.getStartDate()));
    }

    if (condition.getEndDate() != null) {
      predicates.add(cb.lessThanOrEqualTo(root.get("date"), condition.getEndDate()));
    }

    if (condition.getIdAfter() != null) {
      predicates.add(cb.greaterThan(root.get("id"), condition.getIdAfter()));
    }

    cq.where(predicates.toArray(new Predicate[0]));

    // 정렬 처리
    Path<?> sortPath = root.get(condition.getSortField());
    if ("desc".equalsIgnoreCase(condition.getSortDirection())) {
      cq.orderBy(cb.desc(sortPath));
    } else {
      cq.orderBy(cb.asc(sortPath));
    }

    return em.createQuery(cq)
        .setMaxResults(condition.getSize())
        .getResultList();
  }

  @Override
  public Integer countByCondition(IndexDataSearchCondition condition) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<IndexData> root = cq.from(IndexData.class);

    List<Predicate> predicates = new ArrayList<>();

    if (condition.getIndexInfoId() != null) {
      predicates.add(cb.equal(root.get("indexInfo").get("id"), condition.getIndexInfoId()));
    }

    if (condition.getStartDate() != null) {
      predicates.add(cb.greaterThanOrEqualTo(root.get("date"), condition.getStartDate()));
    }

    if (condition.getEndDate() != null) {
      predicates.add(cb.lessThanOrEqualTo(root.get("date"), condition.getEndDate()));
    }

    cq.select(cb.count(root)).where(predicates.toArray(new Predicate[0]));

    return Math.toIntExact(em.createQuery(cq).getSingleResult());
  }

  @Override
  public List<IndexData> findWithCursor(IndexInfo indexInfo, LocalDate startDate,
      LocalDate endDate, Integer cursorId, int size) {

//    String sql =
//        "SELECT id, index_info_id, base_date, source_type, open_price, close_price, high_price, " +
//            "low_price, versus, fluctuation_rate, trading_quantity, trading_price, market_total_amount "
//            +
//            "FROM index_data " +
//            "WHERE (:indexInfoId IS NULL OR index_info_id = :indexInfoId) " +
//            "AND (:startDate IS NULL OR base_date >= :startDate) " +
//            "AND (:endDate IS NULL OR base_date <= :endDate) " +
//            "AND (:cursorId IS NULL OR id > :cursorId) " +
//            "ORDER BY id ASC " +
//            "LIMIT :limit";

    StringBuilder sql = new StringBuilder("""
            SELECT id, index_info_id, base_date, source_type, open_price, close_price, high_price,
                   low_price, versus, fluctuation_rate, trading_quantity, trading_price, 
                   market_total_amount 
              FROM index_data
        """);

    boolean hasWhere = false;

    if (indexInfo.getId() != null) {
      sql.append(" WHERE index_info_id = :indexInfoId");
      hasWhere = true;
    }
    if (startDate != null) {
      sql.append(hasWhere ? " AND" : " WHERE");
      sql.append(" base_date >= :startDate");
      hasWhere = true;
    }
    if (endDate != null) {
      sql.append(hasWhere ? " AND" : " WHERE");
      sql.append(" base_date <= :endDate");
      hasWhere = true;
    }
    if (cursorId != null) {
      sql.append(hasWhere ? " AND" : " WHERE");
      sql.append(" id > :cursorId");
    }
    sql.append(" ORDER BY id ASC LIMIT :limit");

    Query query = em.createNativeQuery(sql.toString());

    if (indexInfo.getId() != null) {
      query.setParameter("indexInfoId", indexInfo.getId());
    }
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    query.setParameter("cursorId", cursorId);
    query.setParameter("limit", size + 1);

    try {
      List<Object[]> rawResults = (List<Object[]>) query.getResultList();

      return rawResults.stream().map(row -> {
        IndexData data = new IndexData();

        // 기본 키
        if (row[0] instanceof Number) {
          data.setId(((Number) row[0]).intValue());
        } else {
          log.warn("row[0] is not a number: {}", row[0]);
        }

        // indexInfo는 ID만 받아오므로 null 처리하거나 별도로 fetch 필요
        // data.setIndexInfo(...); → 생략하거나 Lazy 로드

        if (row[2] instanceof java.sql.Date) {
          java.sql.Date sqlDate = (java.sql.Date) row[2];
          if (sqlDate != null) {
            data.setBaseDate(sqlDate.toLocalDate());
          }
        } else {
          log.warn("row[2] is not java.sql.Date: {}", row[2]);
        }

        if (row[3] != null) {
          data.setSourceType(SourceType.valueOf((String) row[3]));
        }
        data.setIndexInfo(indexInfo);
        data.setOpenPrice((Double) row[4]);
        data.setClosePrice((Double) row[5]);
        data.setHighPrice((Double) row[6]);
        data.setLowPrice((Double) row[7]);
        data.setVersus((Double) row[8]);
        data.setFluctuationRate((Double) row[9]);
        data.setTradingQuantity((Long) row[10]);
        data.setTradingPrice((Long) row[11]);
        data.setMarketTotalAmount((Long) row[12]);

        return data;
      }).toList();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

    // 기본 키
    // indexInfo는 ID만 받아오므로 null 처리하거나 별도로 fetch 필요
    // data.setIndexInfo(...); → 생략하거나 Lazy 로드

  }
}

