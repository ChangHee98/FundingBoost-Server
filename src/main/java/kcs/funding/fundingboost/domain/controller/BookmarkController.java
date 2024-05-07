package kcs.funding.fundingboost.domain.controller;

import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.global.ResponseDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.wishList.MyBookmarkListDto;
import kcs.funding.fundingboost.domain.service.BookmarkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/bookmark")
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("")
    public ResponseDto<MyBookmarkListDto> viewMyFavoriteListDto(@RequestParam("memberId") Long memberId) {
        return ResponseDto.ok(bookmarkService.getMyBookmark(memberId));
    }

    /**
     * 상품 좋아요
     */
    @PostMapping("/like/{itemId}")
    public ResponseDto<CommonSuccessDto> itemLike(@RequestParam(name = "memberId") Long memberId,
                                                  @PathVariable(name = "itemId") Long itemId) {
        return ResponseDto.ok(bookmarkService.toggleItemLike(memberId, itemId));
    }
}
