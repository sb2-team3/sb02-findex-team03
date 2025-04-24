package com.findex.demo.global.pagination.sort;

import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.global.pagination.util.GenericCursor;
import com.querydsl.core.BooleanBuilder;

public class AutoSyncConfigPredicateBuilder {

    public static BooleanBuilder build(QAutoSyncConfig q, GenericCursor cursor) {
        BooleanBuilder builder = new BooleanBuilder();

        if (cursor == null) return builder;

        String field = cursor.getSortField();

        if (field.equals(SortField.INDEX_NAME.toString())) {
            return buildIndexNameCondition(q, cursor);
        } else if (field.equals(SortField.ENABLED.toString())) {
            return buildEnabledCondition(q, cursor);
        } else {
            throw new IllegalArgumentException("지원하지 않는 정렬 필드입니다: " + field);
        }
    }

    private static BooleanBuilder buildIndexNameCondition(QAutoSyncConfig q, GenericCursor cursor) {
        BooleanBuilder builder = new BooleanBuilder();
        String direction = cursor.getSortDirection();
        String sortValue = cursor.getSortValue();

        if (cursor.hasId()) {
            int id = Integer.parseInt(cursor.getIndexInfoId());
            if (direction.equalsIgnoreCase(SortDirection.ASC.toString())) {
                builder.and(
                        q.indexInfo.indexName.gt(sortValue)
                                .or(q.indexInfo.indexName.eq(sortValue).and(q.indexInfo.id.gt(id)))
                );
            } else {
                builder.and(
                        q.indexInfo.indexName.lt(sortValue)
                                .or(q.indexInfo.indexName.eq(sortValue).and(q.indexInfo.id.lt(id)))
                );
            }
        } else {
            if (direction.equalsIgnoreCase(SortDirection.ASC.toString())) {
                builder.and(q.indexInfo.indexName.gt(sortValue));
            } else {
                builder.and(q.indexInfo.indexName.lt(sortValue));
            }
        }

        return builder;
    }

    private static BooleanBuilder buildEnabledCondition(QAutoSyncConfig q, GenericCursor cursor) {
        BooleanBuilder builder = new BooleanBuilder();
        String direction = cursor.getSortDirection();
        Boolean sortValue = Boolean.parseBoolean(cursor.getSortValue());
        if (cursor.hasId()) {
            int id = Integer.parseInt(cursor.getIndexInfoId());
            if (direction.equalsIgnoreCase(SortDirection.ASC.toString())) {
                builder.and(q.enabled.gt(sortValue)
                        .or(q.enabled.eq(sortValue).and(q.indexInfo.id.gt(id))));
            }else{
                builder.and(q.enabled.lt(sortValue)
                        .or(q.enabled.eq(sortValue).and(q.indexInfo.id.lt(id))));
            }
        }else{
            if (direction.equalsIgnoreCase(SortDirection.ASC.toString())) {
                builder.and(q.enabled.gt(sortValue).and(q.enabled.eq(sortValue)));
            }else{
                builder.and(q.enabled.lt(sortValue).and(q.enabled.eq(sortValue)));
            }
        }
        return builder;
    }
}
