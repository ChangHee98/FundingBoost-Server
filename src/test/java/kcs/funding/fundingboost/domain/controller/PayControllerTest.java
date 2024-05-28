package kcs.funding.fundingboost.domain.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.friendFundingPay.FriendPayProcessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayNowDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.MyPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.PayRemainDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.DeliveryDto;
import kcs.funding.fundingboost.domain.dto.response.pay.friendFundingPay.FriendFundingPayingDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyFundingPayViewDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyOrderPayViewDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.GiftHubItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.pay.FriendPayService;
import kcs.funding.fundingboost.domain.service.pay.MyPayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PayController.class)
class PayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendPayService friendPayService;

    @MockBean
    private MyPayService myPayService;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member;
    private Member friend;
    private FundingItem fundingItem;
    private Funding funding;
    private Funding friendFunding;
    private Item item;
    private GiftHubItem giftHubItem;
    private Delivery delivery;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        SecurityContextHolderFixture.setContext(member);
        friend = MemberFixture.member2();
        delivery = DeliveryFixture.address1(member);
        item = ItemFixture.item1();
        funding = FundingFixture.Birthday(member);
        friendFunding = FundingFixture.Graduate(friend);
        fundingItem = FundingItemFixture.fundingItem1(item, funding);
        giftHubItem = GiftHubItemFixture.quantity1(item, member);
    }

    @DisplayName("마이 페이 주문 페이지 조회 & 즉시 결제시 페이지 조회")
    @Test
    void myOrderPayView() throws Exception {
        List<DeliveryDto> deliveryDtoList = List.of(DeliveryDto.fromEntity(delivery));

        MyOrderPayViewDto myOrderPayViewDto = MyOrderPayViewDto.fromEntity(deliveryDtoList, member.getPoint());

        given(myPayService.myOrderPayView(member.getMemberId())).willReturn(myOrderPayViewDto);

        mockMvc.perform(get("/api/v1/pay/view/order")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.deliveryDtoList[0].deliveryId").value(delivery.getDeliveryId()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].customerName").value(delivery.getCustomerName()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].address").value(delivery.getAddress()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].phoneNumber").value(delivery.getPhoneNumber()))
                .andExpect(jsonPath("$.data.point").value(member.getPoint()));
    }

    @DisplayName("마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기")
    @Test
    void myFundingPayView() throws Exception {
        funding = FundingFixture.terminatedFundingSuccess(member, 10000);

        fundingItem = FundingItemFixture.fundingItem2(item, funding);

        List<DeliveryDto> deliveryDtoList = List.of(DeliveryDto.fromEntity(delivery));

        MyFundingPayViewDto myFundingPayViewDto = MyFundingPayViewDto.fromEntity(funding, deliveryDtoList);

        given(myPayService.myFundingPayView(fundingItem.getFundingItemId(), member.getMemberId())).willReturn(
                myFundingPayViewDto);

        mockMvc.perform(get("/api/v1/pay/view/funding/2")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.deliveryDtoList[0].deliveryId").value(delivery.getDeliveryId()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].customerName").value(delivery.getCustomerName()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].address").value(delivery.getAddress()))
                .andExpect(jsonPath("$.data.deliveryDtoList[0].phoneNumber").value(delivery.getPhoneNumber()))
                .andExpect(jsonPath("$.data.point").value(member.getPoint()))
                .andExpect(jsonPath("$.data.collectPrice").value(funding.getCollectPrice()));
    }

    @DisplayName("상품 구매하기")
    @Test
    void payMyOrder() throws Exception {
        ItemPayDto itemPayDto = new ItemPayDto(item.getItemId(), giftHubItem.getGiftHubItemId(), 2);
        List<ItemPayDto> itemPayDtoList = List.of(itemPayDto);
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, delivery.getDeliveryId(), 1000);
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(myPayService.payMyItem(myPayDto, member.getMemberId())).willReturn(expectedResponse);

        String content = objectMapper.writeValueAsString(myPayDto);

        mockMvc.perform(post("/api/v1/pay/order")
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }


    @DisplayName("친구 펀딩 결제 페이지 조회")
    @Test
    void viewFriendsFundingDetail() throws Exception {
        // given
        FriendFundingPayingDto friendFundingPayingDto = FriendFundingPayingDto.fromEntity(friendFunding,
                member.getPoint());

        given(friendPayService.getFriendFundingPay(friendFunding.getFundingId(), member.getMemberId())).willReturn(
                friendFundingPayingDto);

        // when & then
        mockMvc.perform(get("/api/v1/pay/friends/{fundingId}", friendFunding.getFundingId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.friendName")
                        .value(friend.getNickName()))
                .andExpect(jsonPath("$.data.friendProfile")
                        .value(friend.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.totalPrice")
                        .value(friendFunding.getTotalPrice()))
                .andExpect(jsonPath("$.data.presentPrice")
                        .value(friendFunding.getCollectPrice()))
                .andExpect(jsonPath("$.data.myPoint")
                        .value(46000));
    }

    @DisplayName("친구 펀딩 결제하기")
    @Test
    void fundFriend() throws Exception {
        FriendPayProcessDto friendPayProcessDto = new FriendPayProcessDto(10000, 30000);
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(friendPayService.fund(member.getMemberId(), funding.getFundingId(), friendPayProcessDto)).willReturn(
                expectedResponse);

        String content = objectMapper.writeValueAsString(friendPayProcessDto);

        mockMvc.perform(post("/api/v1/pay/friends/{fundingId}", funding.getFundingId())
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("상품 즉시 구매하기")
    @Test
    void payMyOrderNow() throws Exception {
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item.getItemId(), 1, delivery.getDeliveryId(), 1000);
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(myPayService.payMyItemNow(itemPayNowDto, member.getMemberId())).willReturn(expectedResponse);

        String content = objectMapper.writeValueAsString(itemPayNowDto);

        mockMvc.perform(post("/api/v1/pay/order/now")
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("펀딩 상품 구매하기")
    @Test
    void payMyFunding() throws Exception {
        PayRemainDto payRemainDto = new PayRemainDto(1000, delivery.getDeliveryId());
        CommonSuccessDto expectedResponse = new CommonSuccessDto(true);

        given(myPayService.payMyFunding(fundingItem.getFundingItemId(), payRemainDto, member.getMemberId())).willReturn(
                expectedResponse);

        String content = objectMapper.writeValueAsString(payRemainDto);

        mockMvc.perform(post("/api/v1/pay/funding/{fundingItemId}", fundingItem.getFundingItemId())
                        .contentType(APPLICATION_JSON)
                        .content(content)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.isSuccess").value(true));

    }
}