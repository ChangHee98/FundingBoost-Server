package kcs.funding.fundingboost.domain.repository;

import kcs.funding.fundingboost.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
