package com.findex.demo.global.pagination.sort;

import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.pagination.dto.SortField;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;

public class AutoSyncConfigPredicateBuilder {

    public static BooleanBuilder build(QAutoSyncConfig q, Integer indexInfoId,
                                       Boolean enabled,
                                       String cursor,
                                       SortField sortField) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(indexCondition(q, indexInfoId));
        builder.and(enabledCondition(q, enabled));
        builder.and(cursorCondition(q, cursor, sortField));
        return builder;
    }

    private static BooleanExpression cursorCondition(QAutoSyncConfig q, String cursor, SortField sortDirection) {
        if(cursor == null || cursor.isBlank()) return null;

        if("asc".equalsIgnoreCase(sortDirection.getPath())){
            return q.indexInfo.indexName.gt(cursor);
        }
        return q.indexInfo.indexName.lt(cursor);
    }

    private static BooleanExpression enabledCondition(QAutoSyncConfig q, Boolean enabled) {
        if(enabled == null) return null;
        return q.enabled.eq(enabled);
    }

    private static BooleanExpression indexCondition(QAutoSyncConfig q, Integer indexInfoId) {
        if(indexInfoId == null) return null;
        return q.indexInfo.id.eq(indexInfoId);
    }
}
