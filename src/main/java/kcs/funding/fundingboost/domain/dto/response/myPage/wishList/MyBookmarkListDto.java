package kcs.funding.fundingboost.domain.dto.response.myPage.wishList;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import lombok.Builder;

@Builder
public record MyBookmarkListDto(MyPageMemberDto myPageMemberDto, List<BookmarkItemDto> bookmarkItemDtos) {

    public static MyBookmarkListDto fromEntity(MyPageMemberDto myPageMemberDto,
                                               List<BookmarkItemDto> bookmarkItemDtos) {
        return MyBookmarkListDto.builder()
                .myPageMemberDto(myPageMemberDto)
                .bookmarkItemDtos(bookmarkItemDtos)
                .build();
    }

}
