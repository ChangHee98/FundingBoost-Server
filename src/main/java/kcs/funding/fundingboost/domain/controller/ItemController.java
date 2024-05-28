package kcs.funding.fundingboost.domain.controller;


import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.response.shopping.ShopDto;
import kcs.funding.fundingboost.domain.dto.response.shoppingDetail.ItemDetailDto;
import kcs.funding.fundingboost.domain.security.resolver.Login;
import kcs.funding.fundingboost.domain.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemService itemService;

    /**
     * 쇼핑 페이지 조회
     */
    @GetMapping("")
    public ResponseDto<Slice<ShopDto>> ShoppingList(
            @RequestParam(name = "category", required = false) String category
            , @RequestParam(name = "lastItemId", required = false) Long lastItemId
            , Pageable pageable) {
        return ResponseDto.ok(itemService.getItems(lastItemId, category, pageable));
    }

    /**
     * 쇼핑 상세 페이지 조회
     */
    @GetMapping("/{itemId}")
    public ResponseDto<ItemDetailDto> showItemDetail(@Login Long memberId,
                                                     @PathVariable(name = "itemId") Long itemId) {
        return ResponseDto.ok(itemService.getItemDetail(memberId, itemId));
    }
}
