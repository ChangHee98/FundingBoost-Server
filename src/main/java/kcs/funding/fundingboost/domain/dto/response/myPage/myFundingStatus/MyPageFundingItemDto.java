package kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus;

import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.Item;
import lombok.Builder;

@Builder
public record MyPageFundingItemDto(
        Long fundingId,
        String itemName,
        int itemPrice,
        String itemImageUrl,
        String optionName,
        int itemPercent,
        boolean finishedStatus
) {
    public static MyPageFundingItemDto fromEntity(
            Funding funding,
            Item item,
            int itemPercent,
            boolean finishedStatus
    ) {
        return MyPageFundingItemDto.builder()
                .fundingId(funding.getFundingId())
                .itemName(item.getItemName())
                .itemPrice(item.getItemPrice())
                .itemImageUrl(item.getItemImageUrl())
                .optionName(item.getOptionName())
                .itemPercent(itemPercent)
                .finishedStatus(finishedStatus)
                .build();
    }

}
