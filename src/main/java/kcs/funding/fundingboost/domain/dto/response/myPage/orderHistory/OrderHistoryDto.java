package kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record OrderHistoryDto(
        MyPageMemberDto myPageMemberDto,
        List<OrderHistoryItemDto> orderHistoryItemDtoList
) {

    public static OrderHistoryDto fromEntity(MyPageMemberDto myPageMemberDto,
                                             List<OrderHistoryItemDto> orderHistoryItemDtoList) {
        return OrderHistoryDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .orderHistoryItemDtoList(orderHistoryItemDtoList)
                .build();
    }
}
