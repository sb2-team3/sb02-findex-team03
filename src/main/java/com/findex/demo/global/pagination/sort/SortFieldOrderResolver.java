package com.findex.demo.global.pagination.sort;

import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.error.CustomException;
import com.findex.demo.global.error.ErrorCode;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.querydsl.core.types.OrderSpecifier;

public class SortFieldOrderResolver {

    public static OrderSpecifier<?> resolve(QAutoSyncConfig q,
                                            SortField sortField,
                                            SortDirection sortDirection) {

        SortField safeField;
        if (sortField == null) {
            safeField = SortField.INDEX_NAME;
        } else {
            safeField = sortField;
        }

        SortDirection safeDirection;
        if (sortDirection == null) {
            safeDirection = SortDirection.ASC;
        } else {
            safeDirection = sortDirection;
        }

        if (safeField == SortField.INDEX_NAME) {
            if (safeDirection == SortDirection.ASC) {
                return q.indexInfo.indexName.asc();
            } else {
                return q.indexInfo.indexName.desc();
            }
        }

        if (safeField == SortField.ENABLED) {
            if (safeDirection == SortDirection.ASC) {
                return q.enabled.asc();
            } else {
                return q.enabled.desc();
            }
        }

        throw new CustomException(ErrorCode.VALIDATION_ERROR, "정렬 할 수 없습니다");
    }
}
