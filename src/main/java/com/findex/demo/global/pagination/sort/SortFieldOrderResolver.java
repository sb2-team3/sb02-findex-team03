package com.findex.demo.global.pagination.sort;

import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.global.pagination.util.GenericCursor;
import com.querydsl.core.types.OrderSpecifier;

import java.util.ArrayList;
import java.util.List;

public class SortFieldOrderResolver {

    public static OrderSpecifier<?>[] resolve(QAutoSyncConfig q, GenericCursor cursor) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        String sortField = cursor.getSortField();
        String sortDirection = cursor.getSortDirection();
        boolean isAsc = sortDirection.equalsIgnoreCase(SortDirection.ASC.toString());
        boolean hasIdAfter = cursor.hasId();

        if (sortField.equals(SortField.INDEX_NAME)) {
            if (isAsc) {
                orderSpecifiers.add(q.indexInfo.indexName.asc());
                if (hasIdAfter)
                    orderSpecifiers.add(q.indexInfo.id.asc());
            } else {
                orderSpecifiers.add(q.indexInfo.indexName.desc());
                if (hasIdAfter)
                    orderSpecifiers.add(q.indexInfo.id.desc());
            }

        } else if (sortField.equals(SortField.ENABLED)) {
            if (isAsc) {
                orderSpecifiers.add(q.enabled.asc());
                if (hasIdAfter)
                    orderSpecifiers.add(q.indexInfo.id.asc());
            } else {
                orderSpecifiers.add(q.enabled.desc());
                if (hasIdAfter)
                    orderSpecifiers.add(q.indexInfo.id.desc());
            }

        } else if (sortField.equals(SortField.INDEX_INFO_ID)) {
            if (isAsc) {
                orderSpecifiers.add(q.indexInfo.id.asc()); // 고유값이므로 보조정렬 불필요
            } else {
                orderSpecifiers.add(q.indexInfo.id.desc());
            }

        } else {
            throw new CustomException(ErrorCode.VALIDATION_ERROR);
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }
}

