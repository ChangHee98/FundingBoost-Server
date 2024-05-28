package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.dto.response.giftHub.GiftHubDto.createGiftHubDto;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_GIFTHUB_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.AddGiftHubDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.ItemQuantityDto;
import kcs.funding.fundingboost.domain.dto.response.giftHub.GiftHubDto;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.giftHubItem.GiftHubItemRepository;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class GiftHubItemService {
    private final GiftHubItemRepository giftHubItemRepository;

    private final ItemRepository itemRepository;

    private final MemberRepository memberRepository;


    public List<GiftHubDto> getGiftHub(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        List<GiftHubItem> giftHubItems = giftHubItemRepository.findGiftHubItemsByMember(member.getMemberId());

        return giftHubItems.stream()
                .map(giftHubItem -> createGiftHubDto(giftHubItem.getItem(), giftHubItem))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommonSuccessDto addGiftHub(Long itemId, AddGiftHubDto addGiftHubDto, Long memberId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_ITEM));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        GiftHubItem giftHubItem = GiftHubItem.createGiftHubItem(addGiftHubDto.quantity(), item, member);
        giftHubItemRepository.save(giftHubItem);

        return CommonSuccessDto.fromEntity(true);
    }

    @Transactional
    public CommonSuccessDto updateItem(Long gifthubItemId, ItemQuantityDto itemQuantity) {
        GiftHubItem giftHubItem = giftHubItemRepository.findById(gifthubItemId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_GIFTHUB_ITEM));
        giftHubItem.updateQuantity(itemQuantity.quantity());
        return CommonSuccessDto.fromEntity(true);
    }

    @Transactional
    public CommonSuccessDto deleteGiftHubItem(Long memberId, Long giftHubItemId) {
        Optional<GiftHubItem> giftHubItem = giftHubItemRepository.findGiftHubItemByGiftHubItemIdAndMemberId(
                giftHubItemId, memberId);
        if (giftHubItem.isEmpty()) {
            throw new CommonException(NOT_FOUND_GIFTHUB_ITEM);
        }
        giftHubItemRepository.deleteById(giftHubItemId);
        return CommonSuccessDto.fromEntity(true);
    }
}
