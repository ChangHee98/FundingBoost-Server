package kcs.funding.fundingboost.domain.service.pay;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_FUNDING_MONEY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.friendFundingPay.FriendPayProcessDto;
import kcs.funding.fundingboost.domain.dto.response.pay.friendFundingPay.FriendFundingPayingDto;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.contributor.ContributorRepository;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FriendPayServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FundingRepository fundingRepository;
    @Mock
    private ContributorRepository contributorRepository;
    @InjectMocks
    private FriendPayService friendPayService;
    private Member me;
    private Member friend;
    private Funding friendFunding;

    @BeforeEach
    void setup() throws NoSuchFieldException, IllegalAccessException {
        me = MemberFixture.member1();
        friend = MemberFixture.member2();
        friendFunding = FundingFixture.Graduate(friend);
    }


    @DisplayName("친구 펀딩 결제 조회 성공")
    @Test
    public void getFriendFundingPay_Success() {
        //given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(me));
        when(fundingRepository.findById(anyLong())).thenReturn(Optional.of(friendFunding));

        FriendFundingPayingDto expectFriendFundingPayingDto = FriendFundingPayingDto.fromEntity(friendFunding,
                me.getPoint());

        //when
        FriendFundingPayingDto resultDto = friendPayService.getFriendFundingPay(friendFunding.getFundingId(),
                me.getMemberId());

        //then
        assertEquals(expectFriendFundingPayingDto, resultDto);
    }

    @DisplayName("친구 펀딩 결제 조회 실패 : Member Not Found")
    @Test
    public void getFriendFundingPay_MemberNotFound_ThrowsException() {
        //given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        CommonException exception = assertThrows(CommonException.class,
                () -> friendPayService.getFriendFundingPay(friendFunding.getFundingId(), me.getMemberId()));

        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("친구 펀딩 결제 성공")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @ValueSource(ints = {1000, 2000, 3000})
    void fund_Success(int myPoint) throws NoSuchFieldException, IllegalAccessException {
        //given
        Member me = mock(Member.class);
        Funding friendFunding = mock(Funding.class);
        List<Item> items = ItemFixture.items3();
        FundingItemFixture.fundingItems(items, friendFunding);

        FriendPayProcessDto friendPayProcessDto = new FriendPayProcessDto(myPoint, myPoint + 10000);

        when(me.getPoint()).thenReturn(100000);
        // 회원 id로 조회
        when(memberRepository.findById(me.getMemberId())).thenReturn(Optional.of(me));
        // 친구 펀딩을 조회
        when(fundingRepository.findById(friendFunding.getFundingId())).thenReturn(Optional.of(friendFunding));

        int itemPriceSum = items.stream().mapToInt(Item::getItemPrice).sum();
        when(friendFunding.getTotalPrice()).thenReturn(itemPriceSum);
        when(friendFunding.getCollectPrice()).thenReturn(itemPriceSum / 2);

        //when
        CommonSuccessDto result = friendPayService.fund(me.getMemberId(), friendFunding.getFundingId(),
                friendPayProcessDto);

        // then
        // success 응답을 반환해야 한다
        assertTrue(result.isSuccess());
        verify(memberRepository).findById(me.getMemberId());
        verify(fundingRepository).findById(friendFunding.getFundingId());

        // 친구 펀딩의 펀딩액이 myPoint만큼 올라가야 한다
        verify(friendFunding).fund(friendPayProcessDto.fundingPrice());
        verify(me).minusPoint(myPoint);
//        }
    }

    @DisplayName("친구 펀딩 결제 실패 : Member Not Found")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @ValueSource(ints = {1000, 2000, 3000})
    void fund_MemberNotFound_ThrowsException(int myPoint) {
        //given
        FriendPayProcessDto dto = new FriendPayProcessDto(myPoint, myPoint + 1000);

        when(memberRepository.findById(me.getMemberId())).thenThrow(new CommonException(NOT_FOUND_MEMBER));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> friendPayService.fund(me.getMemberId(), friendFunding.getFundingId(), dto));

        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }


    @DisplayName("친구 펀딩 결제 실패 : Member Not Found")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @ValueSource(ints = {1000, 2000, 3000})
    void fund_FundingNotFound_ThrowsException(int myPoint) {
        //given
        FriendPayProcessDto dto = new FriendPayProcessDto(myPoint, myPoint + 1000);

        when(memberRepository.findById(me.getMemberId())).thenReturn(Optional.of(me));
        when(fundingRepository.findById(friendFunding.getFundingId())).thenThrow(
                new CommonException(NOT_FOUND_FUNDING));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> friendPayService.fund(me.getMemberId(), friendFunding.getFundingId(), dto));

        //then
        assertEquals(NOT_FOUND_FUNDING.getMessage(), exception.getMessage());
    }

    @DisplayName("친구 펀딩 결제 실패 : 펀딩 금액 이상 후원할 수 없음")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @ValueSource(ints = {10001, 20000, 30000})
    void fund_InvalidFundingMoney_ThrowsException(int myPoint) throws NoSuchFieldException, IllegalAccessException {
        //given
        Funding friendFunding = FundingFixture.lowPriceRestFunding(friend);
        FriendPayProcessDto dto = new FriendPayProcessDto(myPoint, myPoint + 1000);

        when(memberRepository.findById(me.getMemberId())).thenReturn(Optional.of(me));
        when(fundingRepository.findById(friendFunding.getFundingId())).thenReturn(Optional.of(friendFunding));

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> friendPayService.fund(me.getMemberId(), friendFunding.getFundingId(), dto));

        //then
        assertEquals(INVALID_FUNDING_MONEY.getMessage(), exception.getMessage());
    }
}