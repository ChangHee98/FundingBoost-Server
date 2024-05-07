package kcs.funding.fundingboost.domain.dto.response.myPage.friendFundingHistory;

import java.time.format.DateTimeFormatter;
import kcs.funding.fundingboost.domain.entity.Contributor;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record FriendFundingContributionDto(String nickname,
                                           int price,
                                           String friendProfileImg,
                                           String tag,
                                           String createdDate) {
    public static FriendFundingContributionDto fromEntity(Contributor contributor, Funding friendFunding) {
        return FriendFundingContributionDto.builder()
                .nickname(friendFunding.getMember().getNickName())
                .price(contributor.getContributorPrice())
                .friendProfileImg(friendFunding.getMember().getProfileImgUrl())
                .tag(friendFunding.getTag().getDisplayName())
                .createdDate(friendFunding.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .build();
    }

}
