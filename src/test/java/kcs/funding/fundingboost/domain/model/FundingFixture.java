package kcs.funding.fundingboost.domain.model;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.Tag;

public class FundingFixture {
    public static Funding createBirthdayWithMember1() throws NoSuchFieldException, IllegalAccessException {
        Member member1 = MemberFixture.member1();
        Funding funding = Funding.createFunding(member1, "생일축하해주세욥 3월21일입니닷", Tag.BIRTHDAY, 100000,
                LocalDateTime.now());

        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 1L);
        return funding;
    }

    public static Funding createGraduateWithMember2() throws NoSuchFieldException, IllegalAccessException {
        Member member2 = MemberFixture.member2();
        Funding funding = Funding.createFunding(member2, "졸업축하해주세요 사실 졸업 못했어요ㅠㅠ", Tag.GRADUATE, 300000,
                LocalDateTime.now());
        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 2L);
        return funding;
    }
}
