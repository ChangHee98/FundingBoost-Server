package kcs.funding.fundingboost.domain.service.utils;

import static kcs.funding.fundingboost.domain.service.utils.DateUtils.toDeadlineString;
import static kcs.funding.fundingboost.domain.service.utils.FundingConst.FUNDING_FINISHED_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.time.LocalDateTime;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.Tag;
import kcs.funding.fundingboost.domain.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DateUtilsTest {
    private static final String FUNDING_MESSAGE = "FUNDING_MESSAGE";
    private Member member;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
    }

    @DisplayName("펀딩 마감일이 미래인 경우")
    @Test

    public void testToFundingDeadlineStringWithFutureDate() {
        LocalDateTime futureDate = LocalDateTime.now().plusDays(5);
        Funding funding = Funding.createFunding(member, FUNDING_MESSAGE, Tag.BIRTHDAY, futureDate);
        String expected = "D-5";

        assertEquals(expected, toDeadlineString(funding));
    }

    @DisplayName("펀딩 마감일이 과거인 경우")
    @Test
    public void testToFundingDeadlineStringWithPastDate() {
        LocalDateTime pastDate = LocalDateTime.now().minusSeconds(1);
        Funding funding = Funding.createFunding(member, FUNDING_MESSAGE, Tag.BIRTHDAY, pastDate);

        assertEquals(FUNDING_FINISHED_MESSAGE, toDeadlineString(funding));
    }

    @DisplayName("펀딩 마감일이 오늘인 경우")
    @Test
    public void testToFundingDeadlineStringWithCurrentDate() {
        LocalDateTime today = LocalDateTime.now();
        Funding funding = Funding.createFunding(member, FUNDING_MESSAGE, Tag.BIRTHDAY, today);
        String expected = "D-0";

        assertEquals(expected, toDeadlineString(funding));
    }
}