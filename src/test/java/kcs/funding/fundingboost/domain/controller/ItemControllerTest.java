package kcs.funding.fundingboost.domain.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.shopping.ShopDto;
import kcs.funding.fundingboost.domain.dto.response.shoppingDetail.ItemDetailDto;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Member member;
    private Item item;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        SecurityContextHolderFixture.setContext(member);
        item = ItemFixture.item1();
    }

    @DisplayName("쇼핑 페이지 조회")
    @Test
    void viewShoppingList() throws Exception {
        //given
        List<ShopDto> shopDtoList = Collections.singletonList(ShopDto.createGiftHubDto(item));

        given(itemService.getItems()).willReturn(shopDtoList);

        // when & then
        mockMvc.perform(get("/api/v1/items")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].itemId").value(item.getItemId()))
                .andExpect(jsonPath("$.data[0].itemName").value(item.getItemName()))
                .andExpect(jsonPath("$.data[0].price").value(item.getItemPrice()))
                .andExpect(jsonPath("$.data[0].itemImageUrl").value(item.getItemImageUrl()))
                .andExpect(jsonPath("$.data[0].brandName").value(item.getBrandName()));
    }


    @DisplayName("쇼핑 상세 페이지 조회")
    @Test
    void viewShowItemDetail() throws Exception {
        // given
        ItemDetailDto itemDetailDto = ItemDetailDto.fromEntity(item, true);
        given(itemService.getItemDetail(member.getMemberId(), item.getItemId())).willReturn(itemDetailDto);

        // when & then
        mockMvc.perform(get("/api/v1/items/{itemId}", item.getItemId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.itemThumbnailImageUrl").value(item.getItemImageUrl()))
                .andExpect(jsonPath("$.data.itemName").value(item.getItemName()))
                .andExpect(jsonPath("$.data.itemPrice").value(item.getItemPrice()))
                .andExpect(jsonPath("$.data.bookmark").value(true))
                .andExpect(jsonPath("$.data.option").value(item.getOptionName()));
    }
}