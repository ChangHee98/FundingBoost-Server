package kcs.funding.fundingboost.domain.model;

import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.member.Member;

public class OrderFixture {
    public static Order order1(Member member, Delivery delivery) {
        return Order.createOrder(member, delivery);
    }
}
