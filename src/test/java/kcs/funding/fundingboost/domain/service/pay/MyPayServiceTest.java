package kcs.funding.fundingboost.domain.service.pay;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.BAD_REQUEST_PARAMETER;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_FUNDINGITEM_STATUS;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_ITEM_QUANTITY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_DELIVERY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.ONGOING_FUNDING_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayNowDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.MyPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.PayRemainDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyFundingPayViewDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyOrderPayViewDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.GiftHubItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.DeliveryRepository;
import kcs.funding.fundingboost.domain.repository.ItemRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.OrderRepository;
import kcs.funding.fundingboost.domain.repository.fundingItem.FundingItemRepository;
import kcs.funding.fundingboost.domain.repository.giftHubItem.GiftHubItemRepository;
import kcs.funding.fundingboost.domain.repository.orderItem.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyPayServiceTest {

    @Mock
    private FundingItemRepository fundingItemRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GiftHubItemRepository giftHubItemRepository;

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private MyPayService myPayService;

    private FundingItem fundingItem1;
    private FundingItem fundingItem2;

    private Member member1;
    private Member member2;

    private Funding funding1;
    private Funding funding2;

    private Item item1;

    private Item item2;
    private List<Item> items;

    private Delivery delivery1;
    private Delivery delivery2;

    private GiftHubItem giftHubItem;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        member1 = MemberFixture.member1();
        member2 = MemberFixture.member2();
        funding1 = FundingFixture.terminatedFundingSuccess(member1, 50000);
        funding2 = FundingFixture.BirthdayWithCollectPrice(member1, 50000);
        item1 = ItemFixture.item1();
        item2 = ItemFixture.item2();
        items = ItemFixture.items5();
        fundingItem1 = FundingItemFixture.fundingItem1(item1, funding1);
        fundingItem2 = FundingItemFixture.fundingItem2FinishFunding(item2, funding2);
        delivery1 = DeliveryFixture.address1(member1);
        delivery2 = DeliveryFixture.address1(member2);
        giftHubItem = GiftHubItemFixture.quantity1(item1, member1);
    }

    @DisplayName("마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기 성공")
    @Test
    void myFundingPayView_Success() {
        //given
        when(fundingItemRepository.findFundingItemByFundingItemId(fundingItem1.getFundingItemId()))
                .thenReturn(Optional.of(fundingItem1));
        when(deliveryRepository.findAllByMemberId(member1.getMemberId())).thenReturn(Collections.emptyList());

        //when
        MyFundingPayViewDto result = myPayService.myFundingPayView(fundingItem1.getFundingItemId(),
                member1.getMemberId());

        //then
        assertNotNull(result);
        verify(fundingItemRepository).findFundingItemByFundingItemId(fundingItem1.getFundingItemId());
        verify(deliveryRepository).findAllByMemberId(member1.getMemberId());
    }

    @DisplayName("마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기 실패 : 펀딩 아이템이 이미 배송지 입력이 완료되었거나 포인트로 전환 했거나 전여 금액 결제했을 경우")
    @Test
    void myFundingPayView_Fail_InvalidFundingStatus() {
        //given
        when(fundingItemRepository.findFundingItemByFundingItemId(fundingItem2.getFundingItemId()))
                .thenReturn(Optional.of(fundingItem2));
        when(deliveryRepository.findAllByMemberId(member1.getMemberId())).thenReturn(Collections.emptyList());
        fundingItem2.finishFundingItem();

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.myFundingPayView(fundingItem2.getFundingItemId(), member1.getMemberId()));

        //then
        assertEquals(INVALID_FUNDINGITEM_STATUS.getMessage(), exception.getMessage());
    }

    @DisplayName("마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기 : 펀딩 진행중일 때")
    @Test
    void myFundingPayView_Fail_OngoingFundingError() {
        //given
        when(fundingItemRepository.findFundingItemByFundingItemId(fundingItem2.getFundingItemId()))
                .thenReturn(Optional.of(fundingItem2));
        when(deliveryRepository.findAllByMemberId(member1.getMemberId())).thenReturn(Collections.emptyList());

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.myFundingPayView(fundingItem2.getFundingItemId(), member1.getMemberId()));

        //then
        assertEquals(ONGOING_FUNDING_ERROR.getMessage(), exception.getMessage());
    }

    @DisplayName("마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기 : 로그인 한 사용자와 일치하는 지 확인")
    @Test
    void myFundingPayView_Fail_BadRequestParameter() {
        //given
        when(fundingItemRepository.findFundingItemByFundingItemId(fundingItem1.getFundingItemId()))
                .thenReturn(Optional.of(fundingItem1));
        when(deliveryRepository.findAllByMemberId(member2.getMemberId())).thenReturn(Collections.emptyList());

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.myFundingPayView(fundingItem1.getFundingItemId(), member2.getMemberId()));

        //then
        assertEquals(BAD_REQUEST_PARAMETER.getMessage(), exception.getMessage());
    }

    @DisplayName("마이 페이 주문 페이지 조회 & 즉시 결제시 페이지 조회 성공")
    @Test
    void myOrderPayView_Success() {
        //given
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findAllByMemberId(member1.getMemberId())).thenReturn(List.of(delivery1));

        //when
        MyOrderPayViewDto result = myPayService.myOrderPayView(member1.getMemberId());

        //then
        assertNotNull(result);
        assertEquals(member1.getPoint(), result.point());
        assertEquals(1, result.deliveryListDto().size());
        assertEquals("경기도 성남시 분당구 판교역로 166", result.deliveryListDto().get(0).address());
        assertEquals("장이수", result.deliveryListDto().get(0).customerName());
        assertEquals("010-1234-5678", result.deliveryListDto().get(0).phoneNumber());
        assertEquals(member1.getPoint(), result.point());

        verify(memberRepository, times(1)).findById(member1.getMemberId());
        verify(deliveryRepository, times(1)).findAllByMemberId(member1.getMemberId());
    }

    @DisplayName("마이 페이 주문 페이지 조회 & 즉시 결제시 페이지 조회 실패 : 유저가 존재하지 않는 경우")
    @Test
    void myOrderPayView_Fail_MemberNotFound() {
        //given
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findAllByMemberId(member1.getMemberId())).thenReturn(List.of(delivery1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.myOrderPayView(member2.getMemberId()));

        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 성공")
    @Test
    void payMyItem_Success() {
        //given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()));
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonSuccessDto result = myPayService.payMyItem(myPayDto, member1.getMemberId());

        //then
        assertEquals(true, result.isSuccess());
        verify(memberRepository, times(1)).findById(member1.getMemberId());
        verify(deliveryRepository, times(1)).findById(delivery1.getDeliveryId());
        verify(itemRepository, times(1)).findItemsByItemIds(itemIds);
        verify(giftHubItemRepository, times(1))
                .deleteAllById(myPayDto.itemPayDtoList().stream().map(ItemPayDto::giftHubId).toList());
        verify(orderItemRepository, times(1)).saveAll(any(List.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @DisplayName("상품 구매하기 실패 : 유저가 존재하지 않는 경우")
    @Test
    public void payMyItem_Fail_MemberNotFound() {
        // given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()));
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member2.getMemberId()));

        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 배송지 목록에 존재하지 않는 경우")
    @Test
    public void payMyItem_Fail_DeliveryNotFound() {
        // given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()));
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.empty());
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member1.getMemberId()));

        //then
        assertEquals(NOT_FOUND_DELIVERY.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 유저의 배송지 목록이 아닌 경우")
    @Test
    public void payMyItem_Fail_IncorrectDelivery() {
        // given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()));
        Long deliveryId = delivery2.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery2.getDeliveryId())).thenReturn(Optional.of(delivery2));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member1.getMemberId()));

        //then
        assertEquals(BAD_REQUEST_PARAMETER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 아이템 리스트가 빈 경우")
    @Test
    public void payMyItem_Fail_ItemListIsEmpty() {
        // given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = Collections.emptyList();
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member1.getMemberId()));

        //then
        assertEquals(BAD_REQUEST_PARAMETER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 아이템이 존재하지 않은 경우")
    @Test
    public void payMyItem_Fail_ItemNotFound() {
        // given
        List<Item> expectedItems = Collections.emptyList();
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), giftHubItem.getQuantity()));
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member1.getMemberId()));

        //then
        assertEquals(NOT_FOUND_ITEM.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 구매 수량 0 이하인 경우")
    @Test
    public void payMyItem_Fail_QuantityError() {
        // given
        List<Item> expectedItems = List.of(item1, item2);
        List<ItemPayDto> itemPayDtoList = List.of(
                new ItemPayDto(item1.getItemId(), giftHubItem.getGiftHubItemId(), 0),
                new ItemPayDto(item2.getItemId(), giftHubItem.getGiftHubItemId(), 0));
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        MyPayDto myPayDto = new MyPayDto(itemPayDtoList, deliveryId, usingPoint);

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .collect(Collectors.toList());
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findItemsByItemIds(itemIds)).thenReturn(expectedItems);

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItem(myPayDto, member1.getMemberId()));

        //then
        assertEquals(BAD_REQUEST_PARAMETER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 즉시 구매하기 성공")
    @Test
    void payMyItemNow_Success() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item1.getItemId(), giftHubItem.getQuantity(), deliveryId,
                usingPoint);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));

        //when
        CommonSuccessDto result = myPayService.payMyItemNow(itemPayNowDto, member1.getMemberId());

        //then
        assertTrue(result.isSuccess());
        verify(memberRepository, times(1)).findById(member1.getMemberId());
        verify(deliveryRepository, times(1)).findById(delivery1.getDeliveryId());
        verify(itemRepository, times(1)).findById(item1.getItemId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @DisplayName("상품 즉시 구매하기 실패 : 유저가 존재하지 않는 경우")
    @Test
    void payMyItemNow_Fail_MemberNotFound() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item1.getItemId(), giftHubItem.getQuantity(), deliveryId,
                usingPoint);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.empty());
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItemNow(itemPayNowDto, member1.getMemberId()));

        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 즉시 구매하기 실패 : 배송지 목록에 존재하지 않는 경우")
    @Test
    void payMyItemNow_Fail_DeliveryNotFound() {
        //given
        Long deliveryId = delivery2.getDeliveryId();
        int usingPoint = 10000;
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item1.getItemId(), giftHubItem.getQuantity(), deliveryId,
                usingPoint);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.empty());
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItemNow(itemPayNowDto, member1.getMemberId()));

        //then
        assertEquals(NOT_FOUND_DELIVERY.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 유저의 배송지 목록이 아닌 경우")
    @Test
    public void payMyItemNow_Fail_IncorrectDelivery() {
        // given
        Long deliveryId = delivery2.getDeliveryId();
        int usingPoint = 10000;
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item1.getItemId(), giftHubItem.getQuantity(), deliveryId,
                usingPoint);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery2.getDeliveryId())).thenReturn(Optional.of(delivery2));
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItemNow(itemPayNowDto, member1.getMemberId()));

        //then
        assertEquals(BAD_REQUEST_PARAMETER.getMessage(), exception.getMessage());
    }

    @DisplayName("상품 구매하기 실패 : 구매 수량 0 이하인 경우")
    @Test
    public void payMyItemNow_Fail_ItemListIsEmpty() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        ItemPayNowDto itemPayNowDto = new ItemPayNowDto(item1.getItemId(), 0, deliveryId,
                usingPoint);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyItemNow(itemPayNowDto, member1.getMemberId()));

        //then
        assertEquals(INVALID_ITEM_QUANTITY.getMessage(), exception.getMessage());
    }


    @DisplayName("펀딩 상품 구매하기 성공")
    @Test
    void payMyFunding_Success() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        PayRemainDto payRemainDto = new PayRemainDto(usingPoint, deliveryId);
        when(fundingItemRepository.findFundingItemAndItemByFundingItemId(fundingItem1.getFundingItemId())).thenReturn(
                fundingItem1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));

        //when
        CommonSuccessDto result = myPayService.payMyFunding(fundingItem1.getFundingItemId(), payRemainDto,
                member1.getMemberId());

        //then
        assertTrue(result.isSuccess());
        verify(memberRepository, times(1)).findById(member1.getMemberId());
        verify(fundingItemRepository, times(1)).findFundingItemAndItemByFundingItemId(fundingItem1.getFundingItemId());
        verify(deliveryRepository, times(1)).findById(payRemainDto.deliveryId());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @DisplayName("펀딩 상품 구매하기 실패 : 유저가 존재하지 않는 경우")
    @Test
    void payMyFunding_Fail_MemberNotFound() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        PayRemainDto payRemainDto = new PayRemainDto(usingPoint, deliveryId);
        when(fundingItemRepository.findFundingItemAndItemByFundingItemId(fundingItem1.getFundingItemId())).thenReturn(
                fundingItem1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyFunding(fundingItem1.getFundingItemId(), payRemainDto, member2.getMemberId()));

        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("펀딩 상품 구매하기 실패 : 유저의 배송지 목록이 아닌 경우")
    @Test
    void payMyFunding_Fail_DeliveryNotFound() {
        //given
        Long deliveryId = delivery2.getDeliveryId();
        int usingPoint = 10000;
        PayRemainDto payRemainDto = new PayRemainDto(usingPoint, deliveryId);
        when(fundingItemRepository.findFundingItemAndItemByFundingItemId(fundingItem1.getFundingItemId())).thenReturn(
                fundingItem1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.empty());

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyFunding(fundingItem1.getFundingItemId(), payRemainDto, member1.getMemberId()));

        //then
        assertEquals(NOT_FOUND_DELIVERY.getMessage(), exception.getMessage());
    }

    @DisplayName("펀딩 상품 구매하기 실패 : 펀딩 아이템 상태가 정상적이지 않은 경우")
    @Test
    void payMyFunding_Fail_InvalidFundingItem() {
        //given
        Long deliveryId = delivery1.getDeliveryId();
        int usingPoint = 10000;
        PayRemainDto payRemainDto = new PayRemainDto(usingPoint, deliveryId);
        when(fundingItemRepository.findFundingItemAndItemByFundingItemId(fundingItem1.getFundingItemId())).thenReturn(
                fundingItem1);
        when(memberRepository.findById(member1.getMemberId())).thenReturn(Optional.of(member1));
        when(deliveryRepository.findById(delivery1.getDeliveryId())).thenReturn(Optional.of(delivery1));

        fundingItem1.finishFundingItem();
        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> myPayService.payMyFunding(fundingItem1.getFundingItemId(), payRemainDto, member1.getMemberId()));

        //then
        assertEquals(INVALID_FUNDINGITEM_STATUS.getMessage(), exception.getMessage());
    }
}