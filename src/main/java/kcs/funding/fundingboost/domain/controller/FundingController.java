package kcs.funding.fundingboost.domain.controller;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.request.fundingRegist.RegisterFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.CommonFriendFundingDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingDetailDto;
import kcs.funding.fundingboost.domain.dto.response.fundingRegist.FundingRegisterStatusDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeViewDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.friendFundingHistory.FriendFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.MyFundingHistoryDetailDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.MyFundingStatusDto;
import kcs.funding.fundingboost.domain.security.resolver.Login;
import kcs.funding.fundingboost.domain.service.FundingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FundingController {

    private final FundingService fundingService;

    /**
     * 메인페이지 조회
     */
    @GetMapping("/home")
    public ResponseDto<HomeViewDto> home(@Login Long memberId, Pageable pageable,
                                         @RequestParam(name = "lastItemId", required = false) Long lastItemId) {
        return ResponseDto.ok(fundingService.getMainView(memberId, pageable, lastItemId));
    }

    /**
     * 펀딩 등록페이지 조회
     */
    @GetMapping("/funding")
    public ResponseDto<FundingRegisterStatusDto> viewRegisterFunding(@Login Long memberId) {
        return ResponseDto.ok(fundingService.getRegisterFunding(memberId));
    }

    /**
     * 펀딩 등록하기
     */
    @PostMapping("/funding")
    public ResponseDto<CommonSuccessDto> registerFunding(
            @Login Long memberId,
            @RequestBody RegisterFundingDto registerFundingDto
    ) {

        return ResponseDto.created(fundingService.putFundingAndFundingItem(memberId, registerFundingDto));
    }

    /**
     * 펀딩 종료하기
     */
    @PostMapping("/funding/close/{fundingId}")
    public ResponseDto<CommonSuccessDto> closeFunding(@PathVariable("fundingId") Long fundingId) {
        return ResponseDto.ok(fundingService.terminateFunding(fundingId));
    }

    /**
     * 친구 펀딩 디테일 페이지 조회
     */
    @GetMapping("/funding/friends/{fundingId}")
    public ResponseDto<FriendFundingDetailDto> viewFriendsFundingDetail(@Login Long memberId,
                                                                        @PathVariable("fundingId") Long fundingId) {
        return ResponseDto.ok(fundingService.viewFriendsFundingDetail(fundingId, memberId));
    }

    /**
     * 친구 펀딩 목록 조회
     */
    @GetMapping("/funding/friends")
    public ResponseDto<List<CommonFriendFundingDto>> viewFriendFundingList(
            @Login Long memberId
    ) {
        return ResponseDto.ok(fundingService.getFriendFundingList(memberId));
    }

    /**
     * 펀딩 기간 늘리기
     */
    @PostMapping("/funding/extension/{fundingId}")
    public ResponseDto<CommonSuccessDto> extendMyFunding(@Login Long memberId,
                                                         @PathVariable("fundingId") Long fundingId) {
        return ResponseDto.ok(fundingService.extendFunding(fundingId));
    }

    /**
     * 마이 페이지 조회
     */
    @GetMapping("/funding/my-funding-status")
    public ResponseDto<MyFundingStatusDto> viewMyPage(@Login Long memberId) {
        return ResponseDto.ok(fundingService.getMyFundingStatus(memberId));
    }

    /**
     * 지난 펀딩 이력 조회
     */
    @GetMapping("/funding/history")
    public ResponseDto<MyFundingHistoryDto> viewMyFundingHistory(@Login Long memberId) {
        return ResponseDto.ok(fundingService.getMyFundingHistory(memberId));
    }

    /**
     * 지난 펀딩 이력 상세 조회
     */
    @GetMapping("/funding/history/{fundingId}")
    public ResponseDto<MyFundingHistoryDetailDto> viewMyFundingHistoryDetail(@Login Long memberId,
                                                                             @PathVariable("fundingId") Long fundingId) {
        return ResponseDto.ok(fundingService.getMyFundingHistoryDetails(fundingId));
    }

    /**
     * 친구 펀딩 이력 조회
     */
    @GetMapping("/funding/history/friend")
    public ResponseDto<FriendFundingHistoryDto> viewFriendFundingHistory(@Login Long memberId) {
        return ResponseDto.ok(fundingService.getFriendFundingHistory(memberId));
    }
}
