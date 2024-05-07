package kcs.funding.fundingboost.domain.dto.response.myPage.wishList;


import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record BookmarkItemDto(
        Long itemId,
        String itemThumbnailImageUrl,
        String itemName,
        int itemPrice) {

    public static BookmarkItemDto fromEntity(Item item) {
        return BookmarkItemDto.builder()
                .itemId(item.getItemId())
                .itemThumbnailImageUrl(item.getItemImageUrl())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .build();
    }


}