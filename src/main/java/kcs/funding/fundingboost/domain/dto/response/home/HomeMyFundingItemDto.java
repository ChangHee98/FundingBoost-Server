package kcs.funding.fundingboost.domain.dto.response.home;

import kcs.funding.fundingboost.domain.entity.FundingItem;
import lombok.Builder;

@Builder
public record HomeMyFundingItemDto(String itemImageUrl,
                                   int itemPercent) {

    public static HomeMyFundingItemDto fromEntity(FundingItem fundingItem, int itemPercent) {
        return HomeMyFundingItemDto.builder()
                .itemImageUrl(fundingItem.getItem().getItemImageUrl())
                .itemPercent(itemPercent)
                .build();
    }
}
