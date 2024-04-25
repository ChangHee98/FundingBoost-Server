package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.response.ItemDetailDto;
import kcs.funding.fundingboost.domain.dto.response.ShopDto;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.Bookmark.BookmarkRepository;
import kcs.funding.fundingboost.domain.repository.ItemRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class ItemService {
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final BookmarkRepository bookmarkRepository;

    public List<ShopDto> getItems() {
        List<Item> items = itemRepository.findAll();

        return items.stream()
                .map(ShopDto::createGiftHubDto)
                .collect(Collectors.toList());
    }

    public ItemDetailDto getItemDetail(Long memberId, Long itemId) {

        Optional<Bookmark> bookmark = bookmarkRepository.findBookmarkByMemberAndItem(memberId, itemId);
        if (bookmark.isPresent()) {
            return ItemDetailDto.fromEntity(bookmark.get().getItem(), true);
        } else {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new CommonException(NOT_FOUND_ITEM));

            return ItemDetailDto.fromEntity(item, false);
        }
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
