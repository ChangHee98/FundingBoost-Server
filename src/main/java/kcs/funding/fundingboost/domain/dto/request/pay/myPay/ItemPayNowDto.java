package kcs.funding.fundingboost.domain.dto.request.pay.myPay;

public record ItemPayNowDto(
        Long itemId,
        int quantity,
        Long deliveryId,
        int usingPoint
) {
}
