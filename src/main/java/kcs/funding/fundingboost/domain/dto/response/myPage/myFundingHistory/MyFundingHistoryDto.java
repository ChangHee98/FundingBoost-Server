package kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record MyFundingHistoryDto(
        MyPageMemberDto myPageMemberDto,
        List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtos
) {
    public static MyFundingHistoryDto fromEntity(MyPageMemberDto myPageMemberDto,
                                                 List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtos) {
        return MyFundingHistoryDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .myPageFundingDetailHistoryDtos(myPageFundingDetailHistoryDtos)
                .build();
    }
}
