package kcs.funding.fundingboost.domain.service.utils;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_POINT_LACK;
import static kcs.funding.fundingboost.domain.service.utils.PayUtils.deductPointsIfPossible;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PayUtilsTest {

    private Member member;

    @BeforeEach
    void setUp() {
        member = mock(Member.class);
    }

    @DisplayName("포인트 차감 성공")
    @Test
    void deductPointsSuccessfully() {
        when(member.getPoint()).thenReturn(100);

        deductPointsIfPossible(member, 50);

        verify(member, times(1)).minusPoint(50);
    }


    @DisplayName("포인트 차감 실패 : 차감 포인트가 0원 일 경우")
    @Test
    void deductPointsWithZeroPointsDoesNothing() {
        when(member.getPoint()).thenReturn(100);

        deductPointsIfPossible(member, 0);

        verify(member, never()).minusPoint(anyInt());
    }

    @DisplayName("포인트 차감 실패 : 유저의 포인트보다 차감 포인트가 클 경우")
    @Test
    void deductPointsThrowsExceptionWhenInsufficient() {
        when(member.getPoint()).thenReturn(30);

        CommonException exception = assertThrows(CommonException.class, () -> {
            deductPointsIfPossible(member, 50);
        });

        assertEquals(INVALID_POINT_LACK.getMessage(), exception.getMessage());
    }
}