package kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage;

import kcs.funding.fundingboost.domain.entity.Delivery;


public record DeliveryDto(Long deliveryId,
                          String customerName,
                          String address,
                          String phoneNumber) {


    public static DeliveryDto fromEntity(Delivery delivery) {
        return new DeliveryDto(delivery.getDeliveryId(),
                delivery.getCustomerName(),
                delivery.getAddress(),
                delivery.getPhoneNumber());
    }
}
