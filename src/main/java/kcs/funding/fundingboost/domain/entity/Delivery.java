package kcs.funding.fundingboost.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "delivery")
public class Delivery extends BaseTimeEntity {
    @Id
    @Column(name = "delivery_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;
    @Column(name = "address", length = 200)
    private String address;
    @Column(name = "phone_number", length = 13)
    private String phoneNumber;
    @Column(name = "customer_name", length = 50)
    private String customerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;


    @Column(name = "delivery_status")
    private boolean deliveryStatus;

    public static Delivery createDelivery(String address, String phoneNumber, String customerName, Member member) {
        Delivery delivery = new Delivery();
        delivery.address = address;
        delivery.phoneNumber = phoneNumber;
        delivery.customerName = customerName;
        delivery.member = member;
        return delivery;
    }
}
