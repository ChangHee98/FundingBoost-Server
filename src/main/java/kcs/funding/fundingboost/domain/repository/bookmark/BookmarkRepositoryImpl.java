package kcs.funding.fundingboost.domain.repository.bookmark;

import static kcs.funding.fundingboost.domain.entity.QBookmark.bookmark;
import static kcs.funding.fundingboost.domain.entity.QItem.item;
import static kcs.funding.fundingboost.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Bookmark> findBookmarkByMemberAndItem(Long memberId, Long itemId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(bookmark)
                .join(bookmark.item, item).fetchJoin()
                .join(bookmark.member, member).fetchJoin()
                .where(bookmark.member.memberId.eq(memberId)
                        .and(bookmark.item.itemId.eq(itemId)))
                .fetchOne());
    }
}
