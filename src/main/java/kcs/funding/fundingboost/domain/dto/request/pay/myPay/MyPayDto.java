package kcs.funding.fundingboost.domain.dto.request.pay.myPay;

import java.util.List;

public record MyPayDto(
        List<ItemPayDto> itemPayDtoList,
        Long deliveryId,
        int usingPoint
) {
}
