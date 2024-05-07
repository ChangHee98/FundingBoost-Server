package kcs.funding.fundingboost.domain.dto.response.friendFundingDetail;

import kcs.funding.fundingboost.domain.entity.Contributor;
import lombok.Builder;

@Builder
public record ContributorDto(String contributorName, String contributorProfileImgUrl) {

    public static ContributorDto fromEntity(Contributor contributor) {
        return ContributorDto.builder()
                .contributorName(contributor.getMember().getNickName())
                .contributorProfileImgUrl(contributor.getMember().getProfileImgUrl())
                .build();
    }
}
