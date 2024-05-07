package kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record MyPageDeliveryManageDto(
        MyPageMemberDto myPageMemberDto,
        List<MyPageDeliveryDto> myPageDeliveryDtoList
) {
    public static MyPageDeliveryManageDto fromEntity(
            MyPageMemberDto myPageMemberDto,
            List<MyPageDeliveryDto> myPageDeliveryDtoList
    ) {
        return MyPageDeliveryManageDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .myPageDeliveryDtoList(myPageDeliveryDtoList)
                .build();
    }
}
