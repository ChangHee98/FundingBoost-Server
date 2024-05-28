package kcs.funding.fundingboost.domain.repository.giftHubItem;

import static kcs.funding.fundingboost.domain.entity.QGiftHubItem.giftHubItem;
import static kcs.funding.fundingboost.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GiftHubItemRepositoryImpl implements GiftHubItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<GiftHubItem> findGiftHubItemsByMember(Long memberId) {
        return queryFactory
                .selectFrom(giftHubItem)
                .join(giftHubItem.member, member).fetchJoin()
                .where(member.memberId.eq(memberId))
                .fetch();
    }
}
