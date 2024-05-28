package kcs.funding.fundingboost.domain.repository.orderItem;

import static kcs.funding.fundingboost.domain.entity.QItem.item;
import static kcs.funding.fundingboost.domain.entity.QOrder.order;
import static kcs.funding.fundingboost.domain.entity.QOrderItem.orderItem;
import static kcs.funding.fundingboost.domain.entity.member.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<OrderItem> findLastOrderByMemberId(Long memberId) {
        return queryFactory
                .selectFrom(orderItem)
                .join(orderItem.item, item).fetchJoin()
                .join(orderItem.order, order).fetchJoin()
                .join(order.member, member).fetchJoin()
                .where(member.memberId.eq(memberId))
                .fetch();
    }
}
