package kcs.funding.fundingboost.domain.entity.member;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Column(name = "nick_name", length = 20)
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "member_role")
    private MemberRole memberRole;

    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length = 50)
    private String email;

    @Column(name = "profile_img_url", length = 100)
    private String profileImgUrl;

    @ColumnDefault("0")
    @NotNull
    private int point;

    @Column(length = 100)
    private String kakaoUuid;

    public static Member createSignUpMember(String nickName, String password, String email) {
        Member member = new Member();
        member.nickName = nickName;
        member.password = password;
        member.email = email;
        member.point = 0;
        member.memberRole = MemberRole.ROLE_USER;

        return member;
    }

    public static Member createMember(String nickName, String email, String password, String profileImgUrl,
                                      String kakaoUuid) {
        Member member = new Member();
        member.nickName = nickName;
        member.memberRole = MemberRole.ROLE_USER;
        member.email = email;
        member.password = password;
        member.profileImgUrl = profileImgUrl;
        member.kakaoUuid = kakaoUuid;
        return member;
    }

    //init(포인트 포함)
    public static Member createMemberWithPoint(String nickName, String email, String password, String profileImgUrl,
                                               int point, String kakaoUuid) {
        Member member = new Member();
        member.nickName = nickName;
        member.memberRole = MemberRole.ROLE_USER;
        member.email = email;
        member.password = password;
        member.profileImgUrl = profileImgUrl;
        member.point = point;
        member.kakaoUuid = kakaoUuid;
        return member;
    }

    public void minusPoint(int usingPoint) {
        point -= usingPoint;
    }

    public void plusPoint(int exchangePoint) {
        point += exchangePoint;
    }
}
