package kcs.funding.fundingboost.domain.dto.response.shoppingDetail;

import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record ItemDetailDto(
        String itemThumbnailImageUrl,
        String itemName,
        int itemPrice,
        boolean bookmark,
        String option
) {
    public static ItemDetailDto fromEntity(Item item, boolean bookmark) {
        return ItemDetailDto.builder()
                .itemThumbnailImageUrl(item.getItemImageUrl())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .bookmark(bookmark)
                .option(item.getOptionName())
                .build();
    }
}
