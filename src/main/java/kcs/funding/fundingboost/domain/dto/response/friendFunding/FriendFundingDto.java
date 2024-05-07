package kcs.funding.fundingboost.domain.dto.response.friendFunding;

import kcs.funding.fundingboost.domain.dto.response.common.CommonFriendFundingDto;
import lombok.Builder;

@Builder
public record FriendFundingDto(
        CommonFriendFundingDto commonFriendFundingDto
) {
    public static FriendFundingDto fromEntity(
            CommonFriendFundingDto commonFriendFundingDto
    ) {
        return FriendFundingDto.builder()
                .commonFriendFundingDto(commonFriendFundingDto)
                .build();
    }
}