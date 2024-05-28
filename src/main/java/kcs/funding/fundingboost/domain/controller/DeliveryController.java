package kcs.funding.fundingboost.domain.controller;

import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryManageDto;
import kcs.funding.fundingboost.domain.security.resolver.Login;
import kcs.funding.fundingboost.domain.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    /**
     * 배송지 관리 조회
     */
    @GetMapping("/api/v1/delivery")
    public ResponseDto<MyPageDeliveryManageDto> viewMyDeliveryManagement(@Login Long memberId) {
        return ResponseDto.ok(deliveryService.getMyDeliveryManageList(memberId));
    }
}
