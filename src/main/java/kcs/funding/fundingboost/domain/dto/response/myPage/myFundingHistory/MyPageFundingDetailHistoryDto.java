package kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory;

import java.time.format.DateTimeFormatter;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record MyPageFundingDetailHistoryDto(
        Long fundingId,
        String createdDate,
        String deadLine,
        String itemImageUrl,
        String optionName,
        boolean status,
        int contributorCount,
        int fundingPercent,
        String tag
) {
    public static MyPageFundingDetailHistoryDto fromEntity(Funding funding, int contributor, int fundingPercent) {
        return MyPageFundingDetailHistoryDto.builder()
                .fundingId(funding.getFundingId())
                .createdDate(funding.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .deadLine(funding.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .itemImageUrl(funding.getFundingItems().get(0).getItem().getItemImageUrl())
                .optionName(funding.getFundingItems().get(0).getItem().getOptionName())
                .status(funding.getFundingItems().get(0).getFunding().isFundingStatus())
                .contributorCount(contributor)
                .fundingPercent(fundingPercent)
                .tag(funding.getTag().getDisplayName())
                .build();
    }
}
