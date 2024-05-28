package kcs.funding.fundingboost.domain.service;


import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.myPage.myFundingStatus.TransformPointDto;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private FundingRepository fundingRepository;
    @InjectMocks
    private MemberService memberService;

    private Member member;
    private Funding terminatedFunding;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        terminatedFunding = FundingFixture.Graduate(member);
    }

    @DisplayName("포인트 전환 성공")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @ValueSource(ints = {10000, 20000, 30000})
    void exchangePoint(int myPoint) throws NoSuchFieldException, IllegalAccessException {
        //given

        Member member = mock(Member.class);
        List<Item> items = ItemFixture.items3();
        int finishedFundingItemPrice = items.get(0).getItemPrice() + items.get(1).getItemPrice();

        // input이 되는 Dto를 생성한다
        Funding terminatedFunding = FundingFixture.terminatedFundingFail(member, finishedFundingItemPrice + 10000);
        TransformPointDto transformPointDto = new TransformPointDto(terminatedFunding.getFundingId());

        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, terminatedFunding);

        // 첫번째 fundingItem과 두번째 fundingItem을 펀딩 종료된 상태로 변경해준다
        fundingItems.get(0).completeFunding();
        fundingItems.get(0).finishFundingItem();

        fundingItems.get(1).completeFunding();
        fundingItems.get(1).finishFundingItem();

        // 전환되어야 하는 포인트 = 모은 돈에서 펀딩 종료된 Item의 가격을 뺀 가격
        int expectExchangePoint = terminatedFunding.getCollectPrice() - finishedFundingItemPrice;

        // fundingRepository.FINDMemberByFundingId가 호출되면 종료된 terminatedFunding을 반환한다
        when(fundingRepository.findMemberById(transformPointDto.fundingId())).thenReturn(terminatedFunding);

        //when
        CommonSuccessDto commonSuccessDto = memberService.exchangePoint(transformPointDto);

        //then
        verify(fundingRepository).findMemberById(transformPointDto.fundingId());
        verify(fundingRepository).findMemberById(transformPointDto.fundingId());

        // 포인트 전환 후 fundingItem은 펀딩 종료가 되어야 한다
        assertFalse(terminatedFunding.getFundingItems().get(1).getFunding().isFundingStatus());

        // 포인트 전환 후 나의 포인트가 증가해야 한다
        verify(member).plusPoint(expectExchangePoint);

        // 포인트 전환 후 successDto의 isSuccess는 true를 반환해야 한다
        assertTrue(commonSuccessDto.isSuccess());
    }

    @DisplayName("포인트 전환 실패 - 펀딩을 찾을 수 없음")
    @Test
    void exchangePoint_FundingNotFound() {
        TransformPointDto transformPointDto = new TransformPointDto(terminatedFunding.getFundingId());
        when(fundingRepository.findMemberById(anyLong())).thenReturn(null);

        //when
        CommonException exception = assertThrows(CommonException.class, () -> {
            memberService.exchangePoint(transformPointDto);
        });// 펀딩을 찾지 못했으므로 실패해야 함

        //then
        assertEquals(NOT_FOUND_FUNDING.getMessage(), exception.getMessage());
    }
}