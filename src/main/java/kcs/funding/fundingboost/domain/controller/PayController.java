package kcs.funding.fundingboost.domain.controller;

import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.request.pay.friendFundingPay.FriendPayProcessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.ItemPayNowDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.MyPayDto;
import kcs.funding.fundingboost.domain.dto.request.pay.myPay.PayRemainDto;
import kcs.funding.fundingboost.domain.dto.response.pay.friendFundingPay.FriendFundingPayingDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyFundingPayViewDto;
import kcs.funding.fundingboost.domain.dto.response.pay.myPay.MyOrderPayViewDto;
import kcs.funding.fundingboost.domain.security.resolver.Login;
import kcs.funding.fundingboost.domain.service.pay.FriendPayService;
import kcs.funding.fundingboost.domain.service.pay.MyPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayController {

    private final MyPayService myPayService;
    private final FriendPayService friendPayService;

    /**
     * 마이 페이 주문 페이지 조회 & 즉시 결제시 페이지 조회
     */
    @GetMapping("/view/order")
    public ResponseDto<MyOrderPayViewDto> myOrderPayView(
            @Login Long memberId
    ) {
        return ResponseDto.ok(myPayService.myOrderPayView(memberId));
    }

    /**
     * 마이 페이 펀딩 페이지 조회 펀딩 종료된 펀딩 아이템에 대해서 배송지 입력하기, 전여 금액 결제하기
     */
    @GetMapping("/view/funding/{fundingItemId}")
    public ResponseDto<MyFundingPayViewDto> myFundingPayView(@Login Long memberId,
                                                             @PathVariable(name = "fundingItemId") Long fundingItemId) {
        return ResponseDto.ok(myPayService.myFundingPayView(fundingItemId, memberId));
    }

    /**
     * 상품 구매하기
     */
    @PostMapping("/order")
    public ResponseDto<CommonSuccessDto> payMyOrder(@Login Long memberId, @RequestBody MyPayDto paymentDto) {
        return ResponseDto.ok(myPayService.payMyItem(paymentDto, memberId));
    }

    /**
     * 상품 즉시 구매하기
     */
    @PostMapping("/order/now")
    public ResponseDto<CommonSuccessDto> payMyOrderNow(@Login Long memberId, @RequestBody ItemPayNowDto itemPayNowDto) {
        return ResponseDto.ok(myPayService.payMyItemNow(itemPayNowDto, memberId));
    }

    /**
     * 펀딩 상품 구매하기
     */
    @PostMapping("/funding/{fundingItemId}")
    public ResponseDto<CommonSuccessDto> payMyFunding(@Login Long memberId,
                                                      @PathVariable("fundingItemId") Long fundingItemId,
                                                      @RequestBody PayRemainDto payRemainDto) {
        return ResponseDto.ok(myPayService.payMyFunding(fundingItemId, payRemainDto, memberId));
    }

    /**
     * 친구 펀딩 결제 페이지 조회
     */
    @GetMapping("/friends/{fundingId}")
    public ResponseDto<FriendFundingPayingDto> friendPayView(
            @Login Long memberId,
            @PathVariable("fundingId") Long fundingId) {
        return ResponseDto.ok(friendPayService.getFriendFundingPay(fundingId, memberId));
    }

    /**
     * 친구 펀딩 결제하기
     */
    @PostMapping("/friends/{fundingId}")
    public ResponseDto<CommonSuccessDto> fundFriend(
            @Login Long memberId,
            @PathVariable("fundingId") Long fundingId,
            @RequestBody FriendPayProcessDto friendPayProcessDto) {
        return ResponseDto.ok(friendPayService.fund(memberId, fundingId, friendPayProcessDto));
    }
}
