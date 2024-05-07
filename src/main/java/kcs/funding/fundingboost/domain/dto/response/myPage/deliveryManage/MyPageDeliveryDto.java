package kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage;

import kcs.funding.fundingboost.domain.entity.Delivery;
import lombok.Builder;

@Builder
public record MyPageDeliveryDto(
        String customerName,
        String address,
        String phoneNumber
) {
    public static MyPageDeliveryDto fromEntity(Delivery delivery) {
        return MyPageDeliveryDto.builder()
                .customerName(delivery.getCustomerName())
                .address(delivery.getAddress())
                .phoneNumber(delivery.getPhoneNumber())
                .build();
    }
}
