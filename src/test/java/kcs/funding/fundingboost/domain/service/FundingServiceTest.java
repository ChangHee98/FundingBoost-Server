package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static kcs.funding.fundingboost.domain.service.utils.FundingConst.EXTEND_DEADLINE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.ContributorDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingDetailDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingItemDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyPageFundingDetailHistoryDto;
import kcs.funding.fundingboost.domain.entity.Contributor;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.contributor.ContributorRepository;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import kcs.funding.fundingboost.domain.repository.fundingItem.FundingItemRepository;
import kcs.funding.fundingboost.domain.service.utils.FundingUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FundingServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    FundingRepository fundingRepository;
    @Mock
    FundingItemRepository fundingItemRepository;
    @Mock
    ContributorRepository contributorRepository;
    @InjectMocks
    FundingService fundingService;

    @DisplayName("viewFriendsFundingDetail : funding이 존재하지 않으면 NOT_FOUND_FUNDING 에러를 반환해야 한다")
    @Test
    void viewFriendsFundingDetail_친구펀딩존재하지않음() {
        // given
        Long myMemberId = 1L;
        Long friendFundingId = 1L;
        when(fundingItemRepository.findAllByFundingId(friendFundingId)).thenReturn(new ArrayList<>());

        // when & then
        assertThrows(CommonException.class, () -> fundingService.viewFriendsFundingDetail(friendFundingId, myMemberId));
    }

    @DisplayName("viewFriendsFundingDetail : 펀딩 아이템 목록을 Dto에 넣어주어야 한다")
    @Test
    void viewFriendsFundingDetail_펀딩아이템목록가져오기() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long myMemberId = 1L;
        Member friend = MemberFixture.member2();

        // member1이 펀딩을 연다
        Funding funding = FundingFixture.Birthday(friend);

        // 펀딩에는 3개의 item이 등록되어 있다
        List<Item> items = ItemFixture.items3();
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, funding);

        when(fundingItemRepository.findAllByFundingId(funding.getFundingId())).thenReturn(fundingItems);

        // when
        FriendFundingDetailDto friendFundingDetailDto = fundingService.viewFriendsFundingDetail(funding.getFundingId(),
                myMemberId);
        FriendFundingItemDto friendFundingItemDto1 = friendFundingDetailDto.friendFundingItemList().get(0);
        FriendFundingItemDto friendFundingItemDto2 = friendFundingDetailDto.friendFundingItemList().get(1);
        FriendFundingItemDto friendFundingItemDto3 = friendFundingDetailDto.friendFundingItemList().get(2);

        // then
        verify(fundingItemRepository).findAllByFundingId(funding.getFundingId()); // fundingId로 fundingItem을 조회해야 한다

        // Dto에 있는 fundingItem값과 funding에 등록된 item의 결과는 같아야 한다
        assertEquals(items.get(0).getItemImageUrl(), friendFundingItemDto1.itemImageUrl());
        assertEquals(items.get(0).getItemId(), friendFundingItemDto1.itemId());
        assertEquals(items.get(0).getItemName(), friendFundingItemDto1.itemName());
        assertEquals(items.get(0).getOptionName(), friendFundingItemDto1.optionName());
        assertEquals(items.get(0).getItemPrice(), friendFundingItemDto1.itemPrice());

        assertEquals(items.get(1).getItemImageUrl(), friendFundingItemDto2.itemImageUrl());
        assertEquals(items.get(1).getItemId(), friendFundingItemDto2.itemId());
        assertEquals(items.get(1).getItemName(), friendFundingItemDto2.itemName());
        assertEquals(items.get(1).getOptionName(), friendFundingItemDto2.optionName());
        assertEquals(items.get(1).getItemPrice(), friendFundingItemDto2.itemPrice());

        assertEquals(items.get(2).getItemImageUrl(), friendFundingItemDto3.itemImageUrl());
        assertEquals(items.get(2).getItemId(), friendFundingItemDto3.itemId());
        assertEquals(items.get(2).getItemName(), friendFundingItemDto3.itemName());
        assertEquals(items.get(2).getOptionName(), friendFundingItemDto3.optionName());
        assertEquals(items.get(2).getItemPrice(), friendFundingItemDto3.itemPrice());
    }

    @DisplayName("viewFriendsFundingDetail : 펀딩에 기여한 친구가 존재하면 Dto에 넣어주어야 한다")
    @Test
    void viewFriendsFundingDetail_친구목록호출() throws NoSuchFieldException, IllegalAccessException {
        // member1이 funding을 열고 member2와 member3이 펀딩한 경우

        // given
        Long myMemberId = 4L;
        Member friend = MemberFixture.member1();
        Member friend2 = MemberFixture.member2();
        Member friend3 = MemberFixture.member3();

        // member1이 펀딩을 연다
        Funding funding = FundingFixture.Birthday(friend);

        // member2와 member3은 member1에게 펀딩한 상태이다
        Contributor contributor1 = Contributor.createContributor(1000, friend2, funding);
        Contributor contributor2 = Contributor.createContributor(2000, friend3, funding);
        List<Contributor> contributors = List.of(contributor1, contributor2);

        // 펀딩에는 3개의 item이 등록되어 있다
        List<Item> items = ItemFixture.items3();
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, funding);

        when(fundingItemRepository.findAllByFundingId(funding.getFundingId())).thenReturn(fundingItems);
        when(contributorRepository.findByFundingId(funding.getFundingId())).thenReturn(contributors);

        // when
        FriendFundingDetailDto friendFundingDetailDto = fundingService.viewFriendsFundingDetail(funding.getFundingId(),
                myMemberId);
        ContributorDto contributorDto1 = friendFundingDetailDto.contributorList().get(0);
        ContributorDto contributorDto2 = friendFundingDetailDto.contributorList().get(1);

        // then
        verify(fundingItemRepository).findAllByFundingId(funding.getFundingId()); // fundingId로 fundingItem을 조회해야 한다
        verify(contributorRepository).findByFundingId(funding.getFundingId()); // fundingId로 contributor를 조회해야 한다

        // 생성된 Dto의 값에 contributor의 값이 모두 들어있어야 한다
        assertEquals(friend2.getNickName(), contributorDto1.contributorName());
        assertEquals(friend2.getProfileImgUrl(), contributorDto1.contributorProfileImgUrl());

        assertEquals(friend3.getNickName(), contributorDto2.contributorName());
        assertEquals(friend3.getProfileImgUrl(), contributorDto2.contributorProfileImgUrl());
    }

    @DisplayName("viewFriendsFundingDetail : 펀딩에 기여한 친구가 없으면 Dto에 빈 리스트가 들어가야 한다")
    @Test
    void viewFriendsFundingDetail_친구목록호출_펀딩한친구없음() throws NoSuchFieldException, IllegalAccessException {
        // member1이 funding을 열고 아직 아무도 펀딩하지 않은 경우

        // given
        Long myMemberId = 2L;
        Member friend = MemberFixture.member1();

        // member1이 펀딩을 연다
        Funding funding = FundingFixture.Birthday(friend);

        // 펀딩에는 3개의 item이 등록되어 있다
        List<Item> items = ItemFixture.items3();
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, funding);

        // 펀딩한 사람이 아직 존재하지 않는다
        List<Contributor> contributors = new ArrayList<>();

        when(fundingItemRepository.findAllByFundingId(funding.getFundingId())).thenReturn(fundingItems);
        when(contributorRepository.findByFundingId(funding.getFundingId())).thenReturn(contributors);

        // when
        FriendFundingDetailDto friendFundingDetailDto = fundingService.viewFriendsFundingDetail(funding.getFundingId(),
                myMemberId);

        // then
        assertTrue(friendFundingDetailDto.contributorList().isEmpty());
    }

    @DisplayName("viewFriendsFundingDetail : 펀딩된 현재 퍼센트가 정수로 표시되어야 한다")
    @Test
    void viewFriendsFundingDetail_퍼센트계산() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long myMemberId = 2L;
        Member friend = MemberFixture.member1();
        List<Item> items = ItemFixture.items3();

        int collectPrice = items.stream().mapToInt(Item::getItemPrice).sum() / 2;

        // member1이 펀딩을 연다
        Funding funding = FundingFixture.BirthdayWithCollectPrice(friend, collectPrice);

        // 펀딩에는 3개의 item이 등록되어 있다
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, funding);

        when(fundingItemRepository.findAllByFundingId(funding.getFundingId())).thenReturn(fundingItems);

        // when
        FriendFundingDetailDto friendFundingDetailDto = fundingService.viewFriendsFundingDetail(funding.getFundingId(),
                myMemberId);

        // then
        assertEquals(50, friendFundingDetailDto.contributedPercent());
    }

    @DisplayName("viewFriendsFundingDetail : 친구 펀딩 정보를 가져와야 한다 :: 친구펀딩태그, 친구이름")
    @Test
    void viewFriendsFundingDetail_친구펀딩정보() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long myMemberId = 2L;
        Member friend = MemberFixture.member1();

        // member1이 펀딩을 연다
        Funding funding = FundingFixture.Graduate(friend);

        // 펀딩에는 3개의 item이 등록되어 있다
        List<Item> items = ItemFixture.items3();
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, funding);

        when(fundingItemRepository.findAllByFundingId(funding.getFundingId())).thenReturn(fundingItems);

        // when
        FriendFundingDetailDto friendFundingDetailDto = fundingService.viewFriendsFundingDetail(funding.getFundingId(),
                myMemberId);

        // then
        assertEquals(friend.getNickName(), friendFundingDetailDto.friendName());
        assertEquals(funding.getTag().getDisplayName(), friendFundingDetailDto.fundingTag());
        assertEquals(funding.getMessage(), friendFundingDetailDto.fundingMessage());
        assertEquals(friend.getProfileImgUrl(), friendFundingDetailDto.friendProfileImgUrl());
        assertEquals(funding.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                friendFundingDetailDto.deadline());
    }

    @DisplayName("extendFunding : 아이템이 존재한다면 펀딩 기간이 FundingConst.EXTEND_DEADLINE만큼 증가해야 한다")
    @Test
    void extendFunding_펀딩연장성공() throws NoSuchFieldException, IllegalAccessException {
        // given
        Long fundingId = 1L;
        Funding mockFunding = mock(Funding.class);

        when(fundingRepository.findById(fundingId)).thenReturn(Optional.of(mockFunding));

        // when
        CommonSuccessDto result = fundingService.extendFunding(fundingId);

        // then
        verify(fundingRepository).findById(fundingId); // fundingRepository에서 fundingId로 조회가 발생해야 한다
        verify(mockFunding).extendDeadline(EXTEND_DEADLINE); // funding의 extendDeadline에 EXTEND_DEADLINE인자가 전달돼야 한다
        assertTrue(result.isSuccess()); // 반환 결과는 true여야 한다
    }

    @DisplayName("extendFunding : 펀딩이 존재하지 않으면 NOT_FOUND_FUNDING 예외가 발생해야 한다")
    @Test
    public void extendFunding_펀딩조회실패() throws Exception {
        // given
        Long fundingId = 1L;

        when(fundingRepository.findById(fundingId)).thenReturn(Optional.empty());
        // when & then
        CommonException exception = assertThrows(CommonException.class,
                () -> fundingService.extendFunding(fundingId)); // 펀딩 id가 존재하지 않으면 예외가 발생해야 한다
        assertEquals(NOT_FOUND_FUNDING.getMessage(), exception.getMessage()); // NOT_FOUND_FUNDING 예외가 발생해야 한다
    }

    @DisplayName("getFriendFundingHistory : 펀딩 이력이 있으면 과거 펀딩 이력만 가져와야 한다")
    @Test
    void getFriendFundingHistory_펀딩이력이있는경우() throws NoSuchFieldException, IllegalAccessException {
        // given
        Member member = MemberFixture.member1();

        Item item1 = ItemFixture.item1();
        Item item2 = ItemFixture.item2();

        Funding finishedFunding1 = FundingFixture.terminatedFundingSuccess(member, item1.getItemPrice());
        Funding finishedFunding2 = FundingFixture.terminatedFundingFail(member, item2.getItemPrice());

        // 펀딩 아이템을 펀딩에 추가
        FundingItemFixture.fundingItem1(item1, finishedFunding1);
        FundingItemFixture.fundingItem1(item2, finishedFunding2);

        // 회원이 이전에 만든 종료된 펀딩 목록들
        List<Funding> fundingList = List.of(finishedFunding1, finishedFunding2);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberId(member.getMemberId())).thenReturn(fundingList);
        when(contributorRepository.countContributorsForFunding(finishedFunding1.getFundingId())).thenReturn(10);
        when(contributorRepository.countContributorsForFunding(finishedFunding2.getFundingId())).thenReturn(20);

        // MyPageFundingDetailHistoryDto 생성
        MyPageFundingDetailHistoryDto resultMyPageFundingDetailHistoryDto1 = MyPageFundingDetailHistoryDto.fromEntity(
                finishedFunding1, 10, FundingUtils.calculateFundingPercent(finishedFunding1));
        MyPageFundingDetailHistoryDto resultMyPageFundingDetailHistoryDto2 = MyPageFundingDetailHistoryDto.fromEntity(
                finishedFunding2, 20, FundingUtils.calculateFundingPercent(finishedFunding2));

        // when
        MyFundingHistoryDto myFundingHistoryDto = fundingService.getMyFundingHistory(member.getMemberId());
        List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtos = myFundingHistoryDto.myPageFundingDetailHistoryDtos();
        MyPageFundingDetailHistoryDto myPageFundingDetailHistoryDto1 = myPageFundingDetailHistoryDtos.get(0);
        MyPageFundingDetailHistoryDto myPageFundingDetailHistoryDto2 = myPageFundingDetailHistoryDtos.get(1);

        // then
        assertEquals(2, myPageFundingDetailHistoryDtos.size()); // 끝난 펀딩만 가져와야 한다
        assertEquals(resultMyPageFundingDetailHistoryDto1, myPageFundingDetailHistoryDto1);
        assertEquals(resultMyPageFundingDetailHistoryDto2, myPageFundingDetailHistoryDto2);
    }

    @DisplayName("getFriendFundingHistory : 펀딩 이력이 없으면 빈 리스트를 반환해야 한다")
    @Test
    void getFriendFundingHistory_펀딩이력이없는경우() throws NoSuchFieldException, IllegalAccessException {
        // given
        Member member = MemberFixture.member1();

        // 회원이 이전에 만든 종료된 펀딩 목록들
        List<Funding> fundingList = new ArrayList<>();

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberId(member.getMemberId())).thenReturn(fundingList);

        // when
        MyFundingHistoryDto myFundingHistoryDto = fundingService.getMyFundingHistory(member.getMemberId());
        List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtos = myFundingHistoryDto.myPageFundingDetailHistoryDtos();

        // then
        assertTrue(myPageFundingDetailHistoryDtos.isEmpty());
    }
}