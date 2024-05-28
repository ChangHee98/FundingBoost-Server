package kcs.funding.fundingboost.domain.controller;


import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.AddGiftHubDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.ItemQuantityDto;
import kcs.funding.fundingboost.domain.dto.response.giftHub.GiftHubDto;
import kcs.funding.fundingboost.domain.security.resolver.Login;
import kcs.funding.fundingboost.domain.service.GiftHubItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/gifthub")
public class GiftHubController {

    private final GiftHubItemService giftHubItemService;

    /**
     * Gifthub 페이지 조회
     */
    @GetMapping("")
    public ResponseDto<List<GiftHubDto>> giftHubPage(@Login Long memberId) {
        return ResponseDto.ok(giftHubItemService.getGiftHub(memberId));
    }

    /**
     * Gifthub에 담기
     */
    @PostMapping("/{itemId}")
    public ResponseDto<CommonSuccessDto> addGiftHub(
            @Login Long memberId,
            @PathVariable(name = "itemId") Long itemId,
            @RequestBody AddGiftHubDto addGiftHubDto) {
        return ResponseDto.created(giftHubItemService.addGiftHub(itemId, addGiftHubDto, memberId));
    }

    /**
     * 상품 수량 변경
     */
    @PatchMapping("/quantity/{gifthubItemId}")
    public ResponseDto<CommonSuccessDto> patchGiftHubItem(@PathVariable(name = "gifthubItemId") Long gifthubItemId,
                                                          @RequestBody ItemQuantityDto itemQuantity) {
        return ResponseDto.ok(giftHubItemService.updateItem(gifthubItemId, itemQuantity));
    }

    /**
     * GiftHub 상품 삭제
     */
    @DeleteMapping("{giftHubItemId}")
    public ResponseDto<CommonSuccessDto> deleteGiftHubItem(@Login Long memberId,
                                                           @PathVariable(name = "giftHubItemId") Long giftHubItemId) {
        return ResponseDto.ok(giftHubItemService.deleteGiftHubItem(memberId, giftHubItemId));
    }
}
