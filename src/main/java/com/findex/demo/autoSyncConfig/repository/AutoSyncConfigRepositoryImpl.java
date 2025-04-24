package com.findex.demo.autoSyncConfig.repository;

import com.findex.demo.autoSyncConfig.domain.entity.AutoSyncConfig;
import com.findex.demo.autoSyncConfig.domain.entity.QAutoSyncConfig;
import com.findex.demo.global.pagination.dto.PagedResponse;
import com.findex.demo.global.pagination.dto.SortDirection;
import com.findex.demo.global.pagination.dto.SortField;
import com.findex.demo.global.pagination.sort.AutoSyncConfigPredicateBuilder;
import com.findex.demo.global.pagination.sort.SortFieldOrderResolver;
import com.findex.demo.global.pagination.util.GenericCursor;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
@RequiredArgsConstructor
public class AutoSyncConfigRepositoryImpl implements AutoSyncConfigRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PagedResponse<AutoSyncConfig> getPage(Integer indexInfoId, Boolean enabled, int idAfter, String cursor,
                                                 SortField sortField, SortDirection sortDirection, int size) {
        QAutoSyncConfig q = QAutoSyncConfig.autoSyncConfig;

        GenericCursor genericCursor = GenericCursor.from(cursor);
        log.info("이게 문제인가? " + genericCursor.getIndexInfoId(), genericCursor.getSortField());

        BooleanBuilder where = AutoSyncConfigPredicateBuilder.build(q,  genericCursor);

        log.info(where.toString());

        OrderSpecifier<?>[] orderSpecifiers = SortFieldOrderResolver.resolve(q, genericCursor);

        List<AutoSyncConfig> result = queryFactory
                .selectFrom(q)
                .where(where)
                .orderBy(orderSpecifiers)
                .limit(size + 1)
                .fetch();

        boolean hasNext = result.size() > size;
        if (hasNext) {
            result.remove(result.size() - 1);
        }

        String nextCursor = null;
        String nextIdAfter = null;
        if (hasNext) {
            AutoSyncConfig last = result.get(result.size() - 1);
            nextCursor = createNextCursor(last, sortField, sortDirection, genericCursor);
            nextIdAfter = String.valueOf(last.getId());
        }

        return new PagedResponse<>(result, nextCursor, nextIdAfter, size, result.size(), hasNext);
    }

    private String createNextCursor(AutoSyncConfig last, SortField field, SortDirection direction, GenericCursor cursor) {
        String sortValue;
        String idAfter = null;

        if (field == SortField.INDEX_NAME) {
            sortValue = last.getIndexInfo().getIndexName();
            idAfter = String.valueOf(last.getIndexInfo().getId());
        } else if (field == SortField.ENABLED) {
            sortValue = String.valueOf(last.getEnabled());
            idAfter = String.valueOf(last.getId());
        } else if (field == SortField.INDEX_INFO_ID) {
            sortValue = String.valueOf(last.getIndexInfo().getId());
        } else {
            throw new IllegalArgumentException("지원하지 않는 정렬 필드");
        }

        if (cursor.hasId()) {
            return new GenericCursor(field.toString(), sortValue, idAfter, direction.toString()).toCursorString();
        } else {
            return new GenericCursor(field.toString(), sortValue, null, direction.toString()).toCursorString();
        }
    }
}
