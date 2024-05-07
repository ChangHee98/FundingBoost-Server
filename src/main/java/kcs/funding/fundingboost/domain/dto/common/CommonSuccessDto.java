package kcs.funding.fundingboost.domain.dto.common;

import lombok.Builder;

@Builder
public record CommonSuccessDto(boolean isSuccess) {
    public static CommonSuccessDto fromEntity(boolean isSuccess) {
        return CommonSuccessDto.builder()
                .isSuccess(isSuccess)
                .build();
    }
}
