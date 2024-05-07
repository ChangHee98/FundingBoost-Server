package kcs.funding.fundingboost.domain.repository.relationship;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RelationshipRepositoryRepositoryImpl implements RelationshipRepositoryCustom {

    private final JPAQueryFactory queryFactory;
}
