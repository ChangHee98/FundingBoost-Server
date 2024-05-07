package kcs.funding.fundingboost.domain.dto.response.pay.friendFundingPay;

import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record FriendFundingPayingDto(String friendName,
                                     String friendProfile,
                                     int totalPrice,
                                     int presentPrice,
                                     int myPoint) {

    public static FriendFundingPayingDto fromEntity(Funding funding, int myPoint) {
        return FriendFundingPayingDto.builder()
                .friendName(funding.getMember().getNickName())
                .friendProfile(funding.getMember().getProfileImgUrl())
                .totalPrice(funding.getTotalPrice())
                .presentPrice(funding.getCollectPrice())
                .myPoint(myPoint)
                .build();
    }
}
