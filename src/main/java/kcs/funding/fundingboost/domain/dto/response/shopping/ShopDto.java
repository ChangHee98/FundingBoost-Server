package kcs.funding.fundingboost.domain.dto.response.shopping;

import kcs.funding.fundingboost.domain.entity.Item;

public record ShopDto(Long itemId, String itemName, int price, String itemImageUrl, String brandName) {
    public static ShopDto createGiftHubDto(Item item) {
        return new ShopDto(item.getItemId(), item.getItemName(), item.getItemPrice(),
                item.getItemImageUrl(), item.getBrandName());
    }
}
