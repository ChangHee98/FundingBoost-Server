package kcs.funding.fundingboost.domain.dto.response.myPage.friendFundingHistory;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record FriendFundingHistoryDto(MyPageMemberDto myPageMemberDto,
                                      List<FriendFundingContributionDto> FriendFundingContributionDto
) {
    public static FriendFundingHistoryDto fromEntity(MyPageMemberDto myPageMemberDto,
                                                     List<FriendFundingContributionDto> FriendFundingContributionDto) {
        return FriendFundingHistoryDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .FriendFundingContributionDto(FriendFundingContributionDto)
                .build();
    }
}
