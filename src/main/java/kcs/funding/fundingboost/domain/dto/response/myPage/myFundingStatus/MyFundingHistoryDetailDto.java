package kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record MyFundingHistoryDetailDto(
        MyPageMemberDto myPageMemberDto,
        List<MyPageFundingItemDto> myPageFundingItemDtoList,
        List<ParticipateFriendDto> participateFriendDtoList,
        int totalPercent,
        String createdDate, //2024-05-11
        String deadline        // 2024-06-11
) {
    public static MyFundingHistoryDetailDto createMyFundingHistoryDetailDto(
            MyPageMemberDto myPageMemberDto,
            List<MyPageFundingItemDto> myPageFundingItemDtoList,
            List<ParticipateFriendDto> participateFriendDtoList,
            int totalPercent,
            String createdDate,
            String deadline
    ) {
        return MyFundingHistoryDetailDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .myPageFundingItemDtoList(myPageFundingItemDtoList)
                .participateFriendDtoList(participateFriendDtoList)
                .totalPercent(totalPercent)
                .createdDate(createdDate)
                .deadline(deadline)
                .build();
    }
}
