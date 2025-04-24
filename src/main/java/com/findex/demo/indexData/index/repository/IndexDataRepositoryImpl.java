package com.findex.demo.indexData.index.repository;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import com.findex.demo.indexInfo.domain.entity.SourceType;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
  public List<IndexData> findWithCursor(Integer indexInfoId, LocalDate startDate,
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

    if (indexInfoId != null) {
      sql.append(" WHERE index_info_id = :indexInfoId");
    }
    if (startDate != null) {
      sql.append(" AND base_date >= :startDate");
    }
    if (endDate != null) {
      sql.append(" AND base_date <= :endDate");
    }
    if (cursorId != null) {
      sql.append(" AND id > :cursorId");
    }
    sql.append(" ORDER BY id ASC LIMIT :limit");
    
    Query query = em.createNativeQuery(sql.toString());
    query.setParameter("indexInfoId", indexInfoId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    query.setParameter("cursorId", cursorId);
    query.setParameter("limit", size + 1);


      List<Object[]> rawResults = (List<Object[]>) query.getResultList();

      // 기본 키
      // indexInfo는 ID만 받아오므로 null 처리하거나 별도로 fetch 필요
      // data.setIndexInfo(...); → 생략하거나 Lazy 로드

      return rawResults.stream().map(row -> {
        IndexData data = new IndexData();

        // 기본 키
        data.setId((Integer) row[0]);

        // indexInfo는 ID만 받아오므로 null 처리하거나 별도로 fetch 필요
        // data.setIndexInfo(...); → 생략하거나 Lazy 로드

        try{
          data.
              setBaseDate(((java.sql.Date) row[2])
                  .toLocalDate());
        }catch (Exception e) {
          log.debug("dfddd");
          e.printStackTrace();
        }


        if (row[3] != null) {
          data.setSourceType(SourceType.valueOf((String) row[3]));
        }

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

  }
}

