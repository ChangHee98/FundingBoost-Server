package kcs.funding.fundingboost.domain.dto.response.common;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record CommonFriendFundingDto(
        Long fundingId,
        String nickName,
        String friendProfileImgUrl,
        String friendFundingDeadlineDate,
        String tag,
        int collectPrice,
        int friendFundingPercent,
        List<FriendFundingPageItemDto> friendFundingPageItemDtoList
) {
    public static CommonFriendFundingDto fromEntity(
            Funding funding,
            String deadline,
            int fundingTotalPercent,
            List<FriendFundingPageItemDto> friendFundingPageItemDtoList
    ) {
        return CommonFriendFundingDto.builder()
                .fundingId(funding.getFundingId())
                .nickName(funding.getMember().getNickName())
                .friendProfileImgUrl(funding.getMember().getProfileImgUrl())
                .friendFundingDeadlineDate(deadline)
                .tag(funding.getTag().getDisplayName())
                .collectPrice(funding.getCollectPrice())
                .friendFundingPercent(fundingTotalPercent)
                .friendFundingPageItemDtoList(friendFundingPageItemDtoList)
                .build();
    }
}