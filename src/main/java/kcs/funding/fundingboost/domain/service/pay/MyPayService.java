package kcs.funding.fundingboost.domain.service.pay;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.BAD_REQUEST_PARAMETER;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_FUNDINGITEM_STATUS;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_ITEM_QUANTITY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_DELIVERY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.ONGOING_FUNDING_ERROR;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayNowDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.MyPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.PayRemainDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.DeliveryDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyFundingPayViewDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyOrderPayViewDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.DeliveryRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.OrderRepository;
import kcs.funding.fundingboost.domain.repository.fundingItem.FundingItemRepository;
import kcs.funding.fundingboost.domain.repository.giftHubItem.GiftHubItemRepository;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import kcs.funding.fundingboost.domain.repository.orderItem.OrderItemRepository;
import kcs.funding.fundingboost.domain.service.utils.PayUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPayService {

    private final DeliveryRepository deliveryRepository;
    private final MemberRepository memberRepository;
    private final FundingItemRepository fundingItemRepository;
    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GiftHubItemRepository giftHubItemRepository;

    public MyFundingPayViewDto myFundingPayView(Long fundingItemId, Long memberId) {
        Optional<FundingItem> fundingItem = fundingItemRepository.findFundingItemByFundingItemId(fundingItemId);

        List<DeliveryDto> deliveryDtoList = deliveryRepository.findAllByMemberId(memberId)
                .stream()
                .map(DeliveryDto::fromEntity)
                .toList();
        // 펀딩 아이템이 이미 배송지 입력이 완료되었거나 포인트로 전환 했거나 전여 금액 결제했을 경우
        if (!fundingItem.get().isFinishedStatus()) {
            throw new CommonException(INVALID_FUNDINGITEM_STATUS);
        }
        // 펀딩 진행중일 때
        if (fundingItem.get().getFunding().getDeadline().isAfter(LocalDateTime.now())) {
            throw new CommonException(ONGOING_FUNDING_ERROR);
        }
        // 로그인 한 사용자와 일치하는 지 확인
        if (!fundingItem.get().getFunding().getMember().getMemberId().equals(memberId)) {
            throw new CommonException(BAD_REQUEST_PARAMETER);
        }
        return MyFundingPayViewDto.fromEntity(fundingItem.get().getFunding(), deliveryDtoList);
    }

    public MyOrderPayViewDto myOrderPayView(Long memberId) {
        List<DeliveryDto> deliveryDtoList = deliveryRepository.findAllByMemberId(memberId)
                .stream()
                .map(DeliveryDto::fromEntity)
                .toList();

        int point = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER))
                .getPoint();

        return MyOrderPayViewDto.fromEntity(deliveryDtoList, point);
    }

    @Transactional
    public CommonSuccessDto payMyItem(MyPayDto myPayDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        Delivery delivery = deliveryRepository.findById(myPayDto.deliveryId())
                .orElseThrow(() -> new CommonException((NOT_FOUND_DELIVERY)));

        if (delivery.getMember() != member) {
            throw new CommonException(BAD_REQUEST_PARAMETER);
        }

        if (myPayDto.itemPayDtoList().isEmpty()) {
            throw new CommonException(BAD_REQUEST_PARAMETER);
        }

        PayUtils.deductPointsIfPossible(member, myPayDto.usingPoint());

        List<Long> itemIds = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::itemId)
                .toList();

        Map<Long, Item> itemMap = itemRepository.findItemsByItemIds(itemIds).stream()
                .collect(Collectors.toMap(Item::getItemId, item -> item));

        Order order = Order.createOrder(member, delivery);
        List<OrderItem> orderItems = myPayDto.itemPayDtoList().stream()
                .map(itemPayDto -> {
                    Item item = itemMap.get(itemPayDto.itemId());
                    if (item == null) {
                        throw new CommonException(NOT_FOUND_ITEM);
                    }
                    int quantity = itemPayDto.quantity();
                    if (quantity <= 0) {
                        throw new CommonException(BAD_REQUEST_PARAMETER);
                    }
                    return OrderItem.createOrderItem(order, item, quantity);
                }).toList();

        List<Long> giftHubIdList = myPayDto.itemPayDtoList().stream()
                .map(ItemPayDto::giftHubId).toList();

        giftHubItemRepository.deleteAllById(giftHubIdList);
        orderItemRepository.saveAll(orderItems);
        orderRepository.save(order);

        return CommonSuccessDto.fromEntity(true);
    }

    @Transactional
    public CommonSuccessDto payMyItemNow(ItemPayNowDto itemPayNowDto, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        Delivery delivery = deliveryRepository.findById(itemPayNowDto.deliveryId())
                .orElseThrow(() -> new CommonException((NOT_FOUND_DELIVERY)));

        if (delivery.getMember() != member) {
            throw new CommonException(BAD_REQUEST_PARAMETER);
        }

        if (itemPayNowDto.quantity() == 0) {
            throw new CommonException(INVALID_ITEM_QUANTITY);
        }

        PayUtils.deductPointsIfPossible(member, itemPayNowDto.usingPoint());
        Item item = itemRepository.findById(itemPayNowDto.itemId()).orElseThrow(
                () -> new CommonException(NOT_FOUND_ITEM));
        Order order = Order.createOrder(member, delivery);
        OrderItem orderItem = OrderItem.createOrderItem(order, item, itemPayNowDto.quantity());
        orderRepository.save(order);
        orderItemRepository.save(orderItem);
        return CommonSuccessDto.fromEntity(true);
    }

    @Transactional
    public CommonSuccessDto payMyFunding(Long fundingItemId, PayRemainDto payRemainDto, Long memberId) {
        FundingItem fundingItem = fundingItemRepository.findFundingItemAndItemByFundingItemId(fundingItemId);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        Delivery delivery = deliveryRepository.findById(payRemainDto.deliveryId())
                .orElseThrow(() -> new CommonException(NOT_FOUND_DELIVERY));

        if (!fundingItem.isFinishedStatus()) {
            throw new CommonException(INVALID_FUNDINGITEM_STATUS);
        } else {
            fundingItem.finishFundingItem();
        }

        PayUtils.deductPointsIfPossible(member, payRemainDto.usingPoint());

        Order order = Order.createOrder(member, delivery);
        OrderItem orderItem = OrderItem.createOrderItem(order, fundingItem.getItem(), 1);
        orderRepository.save(order);
        orderItemRepository.save(orderItem);

        return CommonSuccessDto.fromEntity(true);
    }
}