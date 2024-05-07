package kcs.funding.fundingboost.domain.service.utils;

import static kcs.funding.fundingboost.domain.service.utils.FundingUtils.checkFundingFinished;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FundingUtilsTest {
    private Funding funding;
    private FundingItem finishedItem;
    private FundingItem unfinishedItem;

    @BeforeEach
    void setUp() {
        funding = mock(Funding.class);
        finishedItem = mock(FundingItem.class);
        unfinishedItem = mock(FundingItem.class);

        when(finishedItem.isFinishedStatus()).thenReturn(true);
        when(unfinishedItem.isFinishedStatus()).thenReturn(false);
    }

    @DisplayName("모든 FundingItem이 완료되지 않았을 때")
    @Test
    void shouldFinishFundingIfAllItemsAreNotFinished() {
        when(funding.getFundingItems()).thenReturn(Arrays.asList(unfinishedItem, unfinishedItem));

        checkFundingFinished(funding);

        verify(funding, times(1)).finish();
    }

    @DisplayName("하나라도 FundingItem이 완료되었을 때")
    @Test
    void shouldNotFinishFundingIfAnyItemIsFinished() {
        when(funding.getFundingItems()).thenReturn(Arrays.asList(finishedItem, unfinishedItem));

        checkFundingFinished(funding);

        verify(funding, never()).finish();
    }

}