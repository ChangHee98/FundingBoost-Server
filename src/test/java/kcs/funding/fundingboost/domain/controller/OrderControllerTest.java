package kcs.funding.fundingboost.domain.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory.OrderHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory.OrderHistoryItemDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.OrderFixture;
import kcs.funding.fundingboost.domain.model.OrderItemFixture;
import kcs.funding.fundingboost.domain.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    OrderService orderService;

    private Member member;
    private Delivery delivery;
    private Item item;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        delivery = DeliveryFixture.address1(member);
        item = ItemFixture.item1();
        order = OrderFixture.order1(member, delivery);
        orderItem = OrderItemFixture.quantity1(order, item);
    }

    @DisplayName("마이페이지-지난 주문 내역 조회(주문 내역 O)")
    @Test
    void orderHistory() throws Exception {
        List<OrderItem> orderItemList = List.of(orderItem);
        List<OrderHistoryItemDto> orderHistoryItemDtoList = orderItemList.stream()
                .map(OrderHistoryItemDto::fromEntity)
                .toList();
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);

        given(orderService.getOrderHistory(member.getMemberId()))
                .willReturn(OrderHistoryDto.fromEntity(myPageMemberDto, orderHistoryItemDtoList));

        mockMvc.perform(get("/api/v1/order/history")
                        .param("memberId", String.valueOf(member.getMemberId()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName")
                        .value(order.getMember().getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email")
                        .value(order.getMember().getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl")
                        .value(order.getMember().getProfileImgUrl()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList[0].itemName")
                        .value(orderItem.getItem().getItemName()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList[0].itemImageUrl")
                        .value(orderItem.getItem().getItemImageUrl()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList[0].optionName")
                        .value(orderItem.getItem().getOptionName()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList[0].price")
                        .value(orderItem.getItem().getItemPrice()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList[0].quantity")
                        .value(orderItem.getQuantity()));
    }

    @DisplayName("마이페이지-지난 주문 내역 조회(주문 내역 x)")
    @Test
    void orderHistory_NotFoundOrder() throws Exception {
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);

        given(orderService.getOrderHistory(member.getMemberId()))
                .willReturn(OrderHistoryDto.fromEntity(myPageMemberDto, null));

        mockMvc.perform(get("/api/v1/order/history")
                        .param("memberId", String.valueOf(member.getMemberId()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName").value(member.getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email").value(member.getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl").value(member.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.orderHistoryItemDtoList").isEmpty());
    }
}