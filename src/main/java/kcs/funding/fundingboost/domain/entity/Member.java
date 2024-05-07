package kcs.funding.fundingboost.domain.entity;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(length = 100)
    private String password;

    @NotNull
    @Column(length = 50)
    private String email;

    @NotNull
    @Column(name = "profile_img_url", length = 100)
    private String profileImgUrl;

    @ColumnDefault("0")
    @NotNull
    private int point;

    private String refreshToken;

    @Column(length = 100)
    private String kakaoUuid;

    public static Member createMember(String nickName, String email, String password, String profileImgUrl,
                                      String refreshToken, String kakaoUuid) {
        Member member = new Member();
        member.nickName = nickName;
        member.email = email;
        member.password = password;
        member.profileImgUrl = profileImgUrl;
        member.refreshToken = refreshToken;
        member.kakaoUuid = kakaoUuid;
        return member;
    }

    //init(포인트 포함)
    public static Member createMemberWithPoint(String nickName, String email, String password, String profileImgUrl,
                                               int point, String refreshToken, String kakaoUuid) {
        Member member = new Member();
        member.nickName = nickName;
        member.email = email;
        member.password = password;
        member.profileImgUrl = profileImgUrl;
        member.point = point;
        member.refreshToken = refreshToken;
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
