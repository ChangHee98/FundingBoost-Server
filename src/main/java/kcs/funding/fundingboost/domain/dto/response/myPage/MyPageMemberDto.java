package kcs.funding.fundingboost.domain.dto.response.myPage;

import kcs.funding.fundingboost.domain.entity.member.Member;
import lombok.Builder;

@Builder
public record MyPageMemberDto(
        String nickname,
        String email,
        String profileImgUrl,
        int point
) {
    public static MyPageMemberDto fromEntity(Member member) {
        return MyPageMemberDto.builder()
                .nickname(member.getNickName())
                .email(member.getEmail())
                .profileImgUrl(member.getProfileImgUrl())
                .point(member.getPoint())
                .build();
    }
}
