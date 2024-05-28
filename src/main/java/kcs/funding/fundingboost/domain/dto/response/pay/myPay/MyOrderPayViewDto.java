package kcs.funding.fundingboost.domain.dto.response.pay.myPay;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.DeliveryDto;
import lombok.Builder;

@Builder
public record MyOrderPayViewDto(
        List<DeliveryDto> deliveryDtoList,
        int point
) {

    public static MyOrderPayViewDto fromEntity(
            List<DeliveryDto> deliveryDtoList,
            int point) {
        return MyOrderPayViewDto.builder()
                .deliveryDtoList(deliveryDtoList)
                .point(point)
                .build();
    }

}
