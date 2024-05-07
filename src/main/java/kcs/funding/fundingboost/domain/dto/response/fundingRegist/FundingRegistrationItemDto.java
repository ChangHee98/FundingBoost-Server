package kcs.funding.fundingboost.domain.dto.response.fundingRegist;

import kcs.funding.fundingboost.domain.entity.Item;

public record FundingRegistrationItemDto(
        Long itemId,
        Long itemSequence,
        String itemName,
        String itemImageUrl,
        String optionName,
        int itemPrice
) {
    public static FundingRegistrationItemDto createFundingRegistrationItemDto(Item item, Long itemSequence) {
        return new FundingRegistrationItemDto(item.getItemId(), itemSequence, item.getItemName(),
                item.getItemImageUrl(), item.getOptionName(), item.getItemPrice());
    }

}
