package kcs.funding.fundingboost.domain.model;

import static kcs.funding.fundingboost.domain.entity.Tag.BIRTHDAY;
import static kcs.funding.fundingboost.domain.entity.Tag.GRADUATE;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;

public class FundingFixture {


    /**
     * 펀딩액이 모이지 않은 펀딩 생성
     */
    public static Funding Birthday(Member member) throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFunding(member, "생일축하해주세욥 3월21일입니닷", BIRTHDAY,
                LocalDateTime.now().plusDays(15));

        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 1L);

        Field createdDateField = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDateField.setAccessible(true);
        createdDateField.set(funding, LocalDateTime.now());
        return funding;
    }

    /**
     * 펀딩액이 모아진 펀딩 생성
     */
    public static Funding BirthdayWithCollectPrice(Member member, int collectPrice)
            throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFundingForTest(member, "생일축하해주세욥 3월21일입니닷", BIRTHDAY, collectPrice,
                LocalDateTime.now().plusDays(7), true);

        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 2L);

        Field createdDateField = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDateField.setAccessible(true);
        createdDateField.set(funding, LocalDateTime.now());
        return funding;
    }

    /**
     * 펀딩액이 모이지 않은 졸업 펀딩
     */
    public static Funding Graduate(Member member) throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFunding(member, "졸업축하해주세요 사실 졸업 못했어요ㅠㅠ", GRADUATE,
                LocalDateTime.now().plusDays(11));
        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 3L);

        Field createdDateField = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDateField.setAccessible(true);
        createdDateField.set(funding, LocalDateTime.now());
        return funding;
    }

    /**
     * 펀딩액을 모두 모으고 펀딩 종료된 상태
     */
    public static Funding terminatedFundingSuccess(Member member, int collectPrice)
            throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFundingForTest(member, "졸업축하해주세요 사실 졸업 못했어요ㅠㅠ", GRADUATE, collectPrice,
                LocalDateTime.now(), false);
        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 4L);

        Field createdDate = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDate.setAccessible(true);
        createdDate.set(funding, LocalDateTime.now());

        return funding;
    }

    /**
     * 펀딩액을 모두 모으지 못하고 펀딩 종료된 상태
     */
    public static Funding terminatedFundingFail(Member member, int collectPrice)
            throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFundingForTest(member, "졸업축하해주세요 사실 졸업 못했어요ㅠㅠ", GRADUATE, collectPrice,
                LocalDateTime.now(), false);
        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 5L);

        Field createdDate = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDate.setAccessible(true);
        createdDate.set(funding, LocalDateTime.now());

        return funding;
    }

    /**
     * 펀딩액이 얼마 남지 않은 상태
     */
    public static Funding lowPriceRestFunding(Member member)
            throws NoSuchFieldException, IllegalAccessException {
        Funding funding = Funding.createFundingForTest(member, "생일축하해주세욥 3월21일입니닷", BIRTHDAY, 195000,
                LocalDateTime.now().plusDays(4), true);

        Field fundingId = funding.getClass().getDeclaredField("fundingId");
        fundingId.setAccessible(true);
        fundingId.set(funding, 6L);

        Field createdDateField = BaseTimeEntity.class.getDeclaredField("createdDate");
        createdDateField.setAccessible(true);
        createdDateField.set(funding, LocalDateTime.now());
        return funding;
    }
}
