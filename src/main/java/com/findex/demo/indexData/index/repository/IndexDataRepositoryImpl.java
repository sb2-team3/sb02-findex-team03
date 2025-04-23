package com.findex.demo.indexData.index.repository;

import static org.antlr.v4.runtime.tree.xpath.XPath.findAll;

import com.findex.demo.indexData.index.domain.dto.CursorPageResponseIndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataDto;
import com.findex.demo.indexData.index.domain.dto.IndexDataSearchCondition;
import com.findex.demo.indexData.index.domain.entity.IndexData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

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


}

