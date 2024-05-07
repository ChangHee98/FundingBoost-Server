package kcs.funding.fundingboost.domain.dto.response.friendFundingDetail;

import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record FriendFundingItemDto(String itemImageUrl, Long itemId,
                                   String itemName, String optionName, int itemPrice) {

    public static FriendFundingItemDto fromEntity(FundingItem fundingItem) {

        Item item = fundingItem.getItem();
        return FriendFundingItemDto.builder()
                .itemImageUrl(item.getItemImageUrl())
                .itemId(item.getItemId())
                .itemName(item.getItemName())
                .optionName(item.getOptionName())
                .itemPrice(item.getItemPrice())
                .build();
    }
}
