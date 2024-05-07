package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.wishList.BookmarkItemDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.wishList.MyBookmarkListDto;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.ItemRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.bookmark.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public MyBookmarkListDto getMyBookmark(Long memberId) {

        List<BookmarkItemDto> bookmarkItemDtos = bookmarkRepository.findAllByMemberId(memberId).stream()
                .map(bookmark -> BookmarkItemDto.fromEntity(bookmark.getItem())).toList();

        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(
                memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER)));

        return MyBookmarkListDto.fromEntity(myPageMemberDto, bookmarkItemDtos);
    }

    @Transactional
    public CommonSuccessDto toggleItemLike(Long memberId, Long itemId) {
        Optional<Bookmark> optionalBookmark = bookmarkRepository.findBookmarkByMemberAndItem(memberId, itemId);

        if (optionalBookmark.isPresent()) {
            bookmarkRepository.delete(optionalBookmark.get());
        } else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new CommonException(NOT_FOUND_ITEM));

            Bookmark bookmark = Bookmark.createBookmark(member, item);
            bookmarkRepository.save(bookmark);
        }
        return CommonSuccessDto.fromEntity(true);
    }
}
