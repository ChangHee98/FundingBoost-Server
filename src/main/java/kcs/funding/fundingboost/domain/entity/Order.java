package kcs.funding.fundingboost.domain.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @NotNull
    private int totalPrice;


    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "memeber_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Delivery delivery;


    public static Order createOrder(Member member, Delivery delivery) {
        Order order = new Order();
        order.totalPrice = 0;
        order.member = member;
        order.delivery = delivery;
        return order;
    }

    public void plusTotalPrice(int plusPrice) {
        totalPrice += plusPrice;
    }
}
