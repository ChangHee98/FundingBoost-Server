package kcs.funding.fundingboost.domain.dto.response.friendFundingDetail;


import java.time.format.DateTimeFormatter;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record FriendFundingDetailDto(List<FriendFundingItemDto> friendFundingItemList,
                                     List<ContributorDto> contributorList,
                                     String friendName, String friendProfileImgUrl,
                                     String deadline, int contributedPercent,
                                     String fundingTag, String fundingMessage) {


    public static FriendFundingDetailDto fromEntity(List<FriendFundingItemDto> friendFundingItemList, Funding funding,
                                                    List<ContributorDto> contributorList, int contributedPercent
    ) {

        return FriendFundingDetailDto.builder()
                .friendFundingItemList(friendFundingItemList)
                .friendName(funding.getMember().getNickName())
                .friendProfileImgUrl(funding.getMember().getProfileImgUrl())
                .fundingTag(funding.getTag().getDisplayName())
                .fundingMessage(funding.getMessage())
                .contributorList(contributorList)
                .deadline(funding.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .contributedPercent(contributedPercent)
                .build();
    }
}
