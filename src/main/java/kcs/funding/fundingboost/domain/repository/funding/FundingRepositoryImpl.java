package kcs.funding.fundingboost.domain.repository.funding;

import static kcs.funding.fundingboost.domain.entity.QFunding.funding;
import static kcs.funding.fundingboost.domain.entity.QFundingItem.fundingItem;
import static kcs.funding.fundingboost.domain.entity.QItem.item;
import static kcs.funding.fundingboost.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FundingRepositoryImpl implements FundingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Funding> findFundingInfo(Long memberId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(funding)
                .join(funding.fundingItems, fundingItem)
                .join(fundingItem.item, item)
                .join(funding.member, member).fetchJoin()
                .where(funding.member.memberId.eq(memberId)
                        .and(funding.fundingStatus.eq(true)))
                .fetchOne());
    }


    @Override
    public List<Funding> findFundingByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(funding)
                .join(funding.member, member).fetchJoin()
                .where(funding.member.memberId.eq(memberId))
                .orderBy(funding.createdDate.desc())
                .fetch();
    }
}
