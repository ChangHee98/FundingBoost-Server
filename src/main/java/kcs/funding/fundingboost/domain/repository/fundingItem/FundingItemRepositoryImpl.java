package kcs.funding.fundingboost.domain.repository.fundingItem;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FundingItemRepositoryImpl implements FundingItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

}
