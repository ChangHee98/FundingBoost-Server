package kcs.funding.fundingboost.domain.model;

import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;

public class OrderItemFixture {
    public static OrderItem quantity1(Order order, Item item) {
        return OrderItem.createOrderItem(order, item, 1);
    }

    public static OrderItem quantity2(Order order, Item item) {
        return OrderItem.createOrderItem(order, item, 2);
    }

    public static OrderItem quantity5(Order order, Item item) {
        return OrderItem.createOrderItem(order, item, 5);
    }

    public static OrderItem quantity10(Order order, Item item) {
        return OrderItem.createOrderItem(order, item, 10);
    }
}
