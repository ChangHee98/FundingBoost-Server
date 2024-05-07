package kcs.funding.fundingboost.domain.dto.response.home;

import kcs.funding.fundingboost.domain.entity.Member;
import lombok.Builder;

@Builder
public record HomeMemberInfoDto(String nickName, String profile) {

    public static HomeMemberInfoDto fromEntity(Member member) {
        return HomeMemberInfoDto.builder()
                .nickName(member.getNickName())
                .profile(member.getProfileImgUrl())
                .build();
    }
}
