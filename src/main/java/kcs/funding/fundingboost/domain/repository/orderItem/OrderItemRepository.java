package kcs.funding.fundingboost.domain.repository.orderItem;

import kcs.funding.fundingboost.domain.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryCustom {
}
