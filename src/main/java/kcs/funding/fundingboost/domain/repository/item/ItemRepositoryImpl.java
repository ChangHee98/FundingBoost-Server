package kcs.funding.fundingboost.domain.repository.item;

import static kcs.funding.fundingboost.domain.entity.QItem.item;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<Item> findItemsByCategory(Long lastItemId, String category, Pageable pageable) {
        List<Item> items = queryFactory
                .selectFrom(item)
                .where(isCategory(category),
                        ltItemId(lastItemId))
                .orderBy(item.itemId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, items);
    }

    @Override
    public Slice<Item> findItemsBySlice(Long lastItemId, Pageable pageable) {
        List<Item> items = queryFactory
                .selectFrom(item)
                .where(ltItemId(lastItemId))
                .orderBy(item.itemId.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, items);
    }

    // no-offset 방식 처리하는 메서드
    private BooleanExpression ltItemId(Long itemId) {
        if (itemId == null) {
            return null;
        }

        return item.itemId.lt(itemId);
    }

    // 무한 스크롤 방식 처리하는 메서드
    private Slice<Item> checkLastPage(Pageable pageable, List<Item> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression isCategory(String category) {
        if (category == null || category.isEmpty()) {
            return null;
        }
        return item.category.eq(category);
    }
}
