package kcs.funding.fundingboost.domain.controller;

import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.request.myPage.myFundingStatus.TransformPointDto;
import kcs.funding.fundingboost.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    /**
     * 포인트 전환하기
     */
    @PatchMapping("/api/v1/member/point")
    public ResponseDto<CommonSuccessDto> exchangePoint(@RequestBody TransformPointDto transformPointDto,
                                                       @RequestParam("memberId") Long memberId) {
        return ResponseDto.ok(memberService.exchangePoint(transformPointDto));
    }
}
