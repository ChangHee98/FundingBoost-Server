package kcs.funding.fundingboost.domain.dto.response.pay.myPay;


import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.DeliveryDto;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record MyFundingPayViewDto(
        List<DeliveryDto> deliveryDtoList,
        int point,
        int collectPrice) {
    public static MyFundingPayViewDto fromEntity(Funding funding,
                                                 List<DeliveryDto> deliveryDtoList) {
        return MyFundingPayViewDto.builder()
                .deliveryDtoList(deliveryDtoList)
                .point(funding.getMember().getPoint())
                .collectPrice(funding.getCollectPrice())
                .build();
    }
}
