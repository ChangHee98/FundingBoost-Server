package kcs.funding.fundingboost.domain.dto.response.fundingRegist;

import lombok.Builder;

@Builder
public record FundingRegisterStatusDto(
        boolean isRegisterFunding
) {
}
