package kcs.funding.fundingboost.domain.dto.response.giftHub;

import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;

public record GiftHubDto(Long itemId, String itemName, String itemImageUrl,
                         String optionName, int itemPrice, int quantity) {
    public static GiftHubDto createGiftHubDto(Item item, GiftHubItem giftHubItem) {
        return new GiftHubDto(item.getItemId(), item.getItemName(), item.getItemImageUrl(), item.getOptionName(),
                item.getItemPrice(), giftHubItem.getQuantity());
    }
}
