package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.global.pagination.sort.AutoSyncConfigPredicateBuilder;
import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.global.pagination.sort.SortFieldOrderResolver;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutoSyncConfigCursorPageExecutor implements AutoSyncConfigCursorPage{

    private final JPAQueryFactory queryFactory;

    @Override
    public PagedResponse<AutoSyncConfig> getPage(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                 SortField sortField, SortDirection sortDirection, int size) {
        QAutoSyncConfig qAutoSyncConfig = QAutoSyncConfig.autoSyncConfig;

        BooleanBuilder where = AutoSyncConfigPredicateBuilder.build(
                qAutoSyncConfig,indexInfoId,  enabled, cursor, sortField
        );

        OrderSpecifier<?> resolve = SortFieldOrderResolver.resolve(qAutoSyncConfig, sortField, sortDirection);

        queryFactory
                .selectFrom(qAutoSyncConfig)
                .where(where)
                .orderBy(resolve);

        return null;
    }
}
