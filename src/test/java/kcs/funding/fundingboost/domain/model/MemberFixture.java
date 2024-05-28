package kcs.funding.fundingboost.domain.model;

import java.lang.reflect.Field;
import kcs.funding.fundingboost.domain.entity.member.Member;

public class MemberFixture {
    public static Member member1() throws NoSuchFieldException, IllegalAccessException {
        Member member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

        Field memberId = member.getClass().getDeclaredField("memberId");
        memberId.setAccessible(true);
        memberId.set(member, 1L);

        return member;
    }

    public static Member member2() throws NoSuchFieldException, IllegalAccessException {
        Member member = Member.createMemberWithPoint("구태형", "rnxogud136@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg",
                999999999, "aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA");

        Field memberId = member.getClass().getDeclaredField("memberId");
        memberId.setAccessible(true);
        memberId.set(member, 2L);

        return member;
    }

    public static Member member3() throws NoSuchFieldException, IllegalAccessException {
        Member member = Member.createMemberWithPoint("맹인호", "aoddlsgh98@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg",
                200000, "aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ");

        Field memberId = member.getClass().getDeclaredField("memberId");
        memberId.setAccessible(true);
        memberId.set(member, 3L);

        return member;
    }

    public static Member memberWithNoPoint() throws NoSuchFieldException, IllegalAccessException {
        Member member = Member.createMemberWithPoint("양혜인", "helen66626662@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/woGALKKcHt/jiOhwZDs9RTkkXPwNYjxF1/wzruf2_110x110_c.jpg",
                0, "aFtpXm1ZaVtuQnRMeUp9Tn5PY1JiV2JRaF8z");

        Field memberId = member.getClass().getDeclaredField("memberId");
        memberId.setAccessible(true);
        memberId.set(member, 4L);

        return member;
    }
}
