package kcs.funding.fundingboost.domain.dto.response.home;

import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record HomeItemDto(Long itemId,
                          String itemName,
                          int price,
                          String itemImageUrl,
                          String brandName) {

    public static HomeItemDto fromEntity(Item item) {
        return HomeItemDto.builder()
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .price(item.getItemPrice())
                .itemImageUrl(item.getItemImageUrl())
                .brandName(item.getBrandName())
                .build();
    }
}
