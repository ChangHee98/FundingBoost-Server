package kcs.funding.fundingboost.domain.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import kcs.funding.fundingboost.domain.config.SecurityConfig;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.AddGiftHubDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.ItemQuantityDto;
import kcs.funding.fundingboost.domain.dto.response.giftHub.GiftHubDto;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.GiftHubItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.GiftHubItemService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(value = GiftHubController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class GiftHubControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GiftHubItemService giftHubItemService;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;
    private Item item;
    private GiftHubItem giftHubItem;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        item = ItemFixture.item1();
        giftHubItem = GiftHubItemFixture.quantity1(item, member);
        SecurityContextHolderFixture.setContext(member);
    }

    @DisplayName("Gifthub 페이지 조회")
    @Test
    void giftHubPage() throws Exception {
        List<GiftHubDto> giftHubDtoList = Collections.singletonList(
                new GiftHubDto(item.getItemId(), item.getItemName(), item.getItemImageUrl(),
                        item.getOptionName(), item.getItemPrice(), 1, giftHubItem.getGiftHubItemId())
        );

        given(giftHubItemService.getGiftHub(member.getMemberId())).willReturn(giftHubDtoList);

        mockMvc.perform(get("/api/v1/gifthub")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].itemName").value(item.getItemName()))
                .andExpect(jsonPath("$.data[0].itemImageUrl").value(item.getItemImageUrl()))
                .andExpect(jsonPath("$.data[0].optionName").value(item.getOptionName()))
                .andExpect(jsonPath("$.data[0].itemPrice").value(item.getItemPrice()))
                .andExpect(jsonPath("$.data[0].quantity").value(1));
    }

    @DisplayName("Gifthub에 담기")
    @Test
    void addGiftHub() throws Exception {
        AddGiftHubDto addGiftHubDto = new AddGiftHubDto(1);
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(giftHubItemService.addGiftHub(any(Long.class), any(AddGiftHubDto.class), any(Long.class)))
                .willReturn(expectedResponse);

        String content = objectMapper.writeValueAsString(addGiftHubDto);

        mockMvc.perform(post("/api/v1/gifthub/{itemId}", item.getItemId())
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("상품 수량 변경")
    @Test
    void patchGiftHubItem() throws Exception {
        ItemQuantityDto itemQuantity = new ItemQuantityDto(5);
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(giftHubItemService.updateItem(any(Long.class), any(ItemQuantityDto.class)))
                .willReturn(expectedResponse);

        mockMvc.perform(patch("/api/v1/gifthub/quantity/{gifthubItemId}", giftHubItem.getGiftHubItemId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemQuantity))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("GiftHub 상품 삭제")
    @Test
    void deleteGiftHubItem() throws Exception {
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(giftHubItemService.deleteGiftHubItem(member.getMemberId(), giftHubItem.getGiftHubItemId()))
                .willReturn(expectedResponse);

        mockMvc.perform(delete("/api/v1/gifthub/{giftHubItemId}", giftHubItem.getGiftHubItemId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }
}