package kcs.funding.fundingboost.domain.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.wishList.BookmarkItemDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.wishList.MyBookmarkListDto;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.BookmarkService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookmarkController.class)
class BookmarkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookmarkService bookmarkService;

    private Member member;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        SecurityContextHolderFixture.setContext(member);
    }

    @DisplayName("viewMyBookmarkListDto: 북마크 조회")
    @Test
    void viewMyBookmarkListDto() throws Exception {
        //given
        MyPageMemberDto myPageMemberDto = new MyPageMemberDto(member.getNickName(), member.getEmail(),
                member.getProfileImgUrl(), member.getPoint());

        List<Item> items = ItemFixture.items5();

        List<BookmarkItemDto> bookmarkItemDtos = new ArrayList<>();

        for (Item item : items) {
            bookmarkItemDtos.add(BookmarkItemDto.fromEntity(item));
        }

        MyBookmarkListDto expectedResponse = MyBookmarkListDto.fromEntity(myPageMemberDto, bookmarkItemDtos);
        given(bookmarkService.getMyBookmark(member.getMemberId())).willReturn(expectedResponse);
        //when & then
        mockMvc.perform(get("/api/v1/bookmark")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName").value(myPageMemberDto.nickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email").value(myPageMemberDto.email()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl").value(myPageMemberDto.profileImgUrl()))
                .andExpect(jsonPath("$.data.myPageMemberDto.point").value(myPageMemberDto.point()))
                .andExpect(jsonPath("$.data.bookmarkItemDtos[2].itemId").value(bookmarkItemDtos.get(2).itemId()))
                .andExpect(jsonPath("$.data.bookmarkItemDtos[2].itemThumbnailImageUrl").value(
                        bookmarkItemDtos.get(2).itemThumbnailImageUrl()))
                .andExpect(jsonPath("$.data.bookmarkItemDtos[4].itemId").value(bookmarkItemDtos.get(4).itemId()))
                .andExpect(jsonPath("$.data.bookmarkItemDtos[4].itemThumbnailImageUrl").value(
                        bookmarkItemDtos.get(4).itemThumbnailImageUrl()));
    }

    @DisplayName("itemLike: 상품 좋아요")
    @Test
    void itemLike() throws Exception {
        //given
        CommonSuccessDto expectedResponse = CommonSuccessDto.fromEntity(true);

        Item item = ItemFixture.item1();

        when(bookmarkService.toggleItemLike(member.getMemberId(), item.getItemId())).thenReturn(expectedResponse);

        //when $ then
        mockMvc.perform(post("/api/v1/bookmark/like/{itemId}", item.getItemId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

}