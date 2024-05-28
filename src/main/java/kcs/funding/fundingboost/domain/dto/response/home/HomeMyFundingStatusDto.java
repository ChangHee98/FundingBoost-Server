package kcs.funding.fundingboost.domain.dto.response.home;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.Funding;
import lombok.Builder;

@Builder
public record HomeMyFundingStatusDto(Long fundingId,
                                     String deadline,
                                     int totalPercent,
                                     String tag,
                                     List<HomeMyFundingItemDto> homeMyFundingItemDtoList) {

    public static HomeMyFundingStatusDto fromEntity(Funding funding,
                                                    String deadline,
                                                    int totalPercent,
                                                    List<HomeMyFundingItemDto> homeMyFundingItemDtoList) {
        return HomeMyFundingStatusDto.builder()
                .fundingId(funding.getFundingId())
                .deadline(deadline)
                .totalPercent(totalPercent)
                .tag(funding.getTag().getDisplayName())
                .homeMyFundingItemDtoList(homeMyFundingItemDtoList)
                .build();
    }

}
