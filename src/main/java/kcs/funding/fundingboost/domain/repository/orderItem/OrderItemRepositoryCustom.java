package kcs.funding.fundingboost.domain.repository.orderItem;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.OrderItem;

public interface OrderItemRepositoryCustom {
    List<OrderItem> findLastOrderByMemberId(Long memberId);
}
