package kcs.funding.fundingboost.domain.model;

import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.Order;

public class OrderFixture {
    public static Order order1(Member member, Delivery delivery) {
        return Order.createOrder(member, delivery);
    }
}
