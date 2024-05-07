package kcs.funding.fundingboost.domain.dto.response.common;

import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record FriendFundingPageItemDto(
        int itemPrice,
        String itemImageUrl
) {
    public static FriendFundingPageItemDto fromEntity(Item item) {
        return FriendFundingPageItemDto.builder()
                .itemPrice(item.getItemPrice())
                .itemImageUrl(item.getItemImageUrl())
                .build();
    }
}