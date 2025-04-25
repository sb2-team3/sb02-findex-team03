package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class AutoSyncConfigRepositoryImpl implements
        AutoSyncConfigRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PagedResponse<AutoSyncConfig> getPage(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                 SortField sortField, SortDirection sortDirection, int size) {
        QAutoSyncConfig q = QAutoSyncConfig.autoSyncConfig;

        BooleanBuilder where = buildWhereClause(q, indexInfoId, enabled, idAfter);
        OrderSpecifier<?> orderSpecifier = resolveOrderSpecifier(q, sortField, sortDirection);

        JPAQuery<AutoSyncConfig> query = queryFactory.selectFrom(q);


        List<AutoSyncConfig> result = query
                .select(q)
                .leftJoin(q.indexInfo).fetchJoin()
                .where(where)
                .orderBy(orderSpecifier)
                .limit(size + 1)
                .fetch();


        boolean hasNext = false;
        if (result.size() > size) {
            result.remove(result.size() - 1);
            hasNext = true;
        }

        String nextIdAfter = null;
        if (!result.isEmpty()) {
            nextIdAfter = String.valueOf(result.get(result.size() - 1).getId());
        }

        return new PagedResponse<>(
                result,
                null,
                nextIdAfter,
                size,
                result.size(),
                hasNext
        );
    }

    private BooleanBuilder buildWhereClause(QAutoSyncConfig q, Integer indexInfoId, Boolean enabled, int idAfter) {
        BooleanBuilder where = new BooleanBuilder();
        boolean hasCondition = false;
        if (indexInfoId != null) {
            where.and(q.indexInfo.id.eq(indexInfoId));
            hasCondition = true;
        }
        if (enabled != null) {
            where.and(q.enabled.eq(enabled));
            hasCondition = true;
        }
        if (idAfter > 0) {
            where.and(q.id.gt(idAfter));
            hasCondition = true;
        }

        if (!hasCondition) {
            where.and(q.indexInfo.id.isNotNull());
        }

        return where;
    }

    private OrderSpecifier<?> resolveOrderSpecifier(QAutoSyncConfig q, SortField sortField, SortDirection sortDirection) {
        if (SortField.INDEX_NAME.equals(sortField)) {
            if (SortDirection.DESC.equals(sortDirection)) {
                return q.indexInfo.indexName.desc();
            }
            return q.indexInfo.indexName.asc();
        }
        if (SortField.ENABLED.equals(sortField)) {
            if (SortDirection.DESC.equals(sortDirection)) {
                return q.enabled.desc();
            }
            return q.enabled.asc();
        }
        if (SortField.INDEX_INFO_ID.equals(sortField)) {
            if (SortDirection.DESC.equals(sortDirection)) {
                return q.indexInfo.id.desc();
            }
            return q.indexInfo.id.asc();
        }
        return q.indexInfo.id.asc();
    }
}
