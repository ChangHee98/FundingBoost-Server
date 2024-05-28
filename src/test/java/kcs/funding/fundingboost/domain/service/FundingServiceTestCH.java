package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.fundingRegist.RegisterFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.CommonFriendFundingDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyPageFundingDetailHistoryDto;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Relationship;
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
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import kcs.funding.fundingboost.domain.repository.relationship.RelationshipRepository;
import kcs.funding.fundingboost.domain.service.utils.FundingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FundingServiceTestCH {

    @InjectMocks
    private FundingService fundingService;

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private FundingRepository fundingRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private FundingItemRepository fundingItemRepository;

    @Mock
    private RelationshipRepository relationshipRepository;

    @Mock
    private ContributorRepository contributorRepository;

    private Member member;
    private Item item1;
    private Item item2;


    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        item1 = ItemFixture.item1();
        item2 = ItemFixture.item2();
    }

    @DisplayName("펀딩 등록하기")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @CsvSource({"'1,2', '생일 선물 주세요', '생일', '2024-05-15'"})
    void putFundingAndFundingItem_Success(String itemIdListString, String fundingMessage, String tag,
                                          String deadlineString) {
        //given
        List<Long> itemIdList = Arrays.stream(itemIdListString.split(","))
                .map(Long::parseLong)
                .toList();
        LocalDate deadline = LocalDate.parse(deadlineString);
        RegisterFundingDto registerFundingDto = new RegisterFundingDto(itemIdList, fundingMessage, tag, deadline);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        //when
        CommonSuccessDto commonSuccessDto = fundingService.putFundingAndFundingItem(member.getMemberId(),
                registerFundingDto);

        //then
        assertNotNull(commonSuccessDto);
        assertTrue(commonSuccessDto.isSuccess());
        verify(fundingRepository, times(1)).save(any());
    }


    @DisplayName("펀딩 등록하기-실패(사용자를 찾을 수 없음)")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @CsvSource({"'1,2', '생일 선물 주세요', '생일', '2024-05-15'"})
    void putFundingAndFundingItem_NotFoundMember(String itemIdListString, String fundingMessage, String tag,
                                                 String deadlineString) {
        //given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
        List<Long> itemIdList = Arrays.stream(itemIdListString.split(","))
                .map(Long::parseLong)
                .toList();
        LocalDate deadline = LocalDate.parse(deadlineString);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));
        RegisterFundingDto registerFundingDto = new RegisterFundingDto(itemIdList, fundingMessage, tag, deadline);

        //when
        CommonException exception = assertThrows(CommonException.class, () -> {
            fundingService.putFundingAndFundingItem(member.getMemberId(), registerFundingDto);
        });
        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    @DisplayName("펀딩 등록하기-실패(아이템을 찾을 수 없음)")
    @ParameterizedTest(name = "{index} {displayName} arguments = {arguments}")
    @CsvSource({"'1,2', '생일 선물 주세요', '생일', '2024-05-15'"})
    void putFundingAndFundingItem_NotFoundItem(String itemIdListString, String fundingMessage, String tag,
                                               String deadlineString) {
        //given
        List<Long> itemIdList = Arrays.stream(itemIdListString.split(","))
                .map(Long::parseLong)
                .toList();
        LocalDate deadline = LocalDate.parse(deadlineString);

        RegisterFundingDto registerFundingDto = new RegisterFundingDto(itemIdList, fundingMessage, tag, deadline);

        //when
        CommonException exception = assertThrows(CommonException.class, () ->
                fundingService.putFundingAndFundingItem(member.getMemberId(), registerFundingDto));
        //then
        assertEquals(NOT_FOUND_ITEM.getMessage(), exception.getMessage());
    }

    @DisplayName("펀딩 종료하기-성공")
    @Test
    void terminateFunding_Success() throws NoSuchFieldException, IllegalAccessException {
        //given
        Funding funding = FundingFixture.Graduate(member);
        String expectDeadline = LocalDate.now().toString();

        // member에 대한 funding 생성
        when(fundingRepository.findById(funding.getFundingId())).thenReturn(Optional.of(funding));

        //when
        CommonSuccessDto commonSuccessDto = fundingService.terminateFunding(funding.getFundingId());
        //then
        assertNotNull(commonSuccessDto);
        assertTrue(commonSuccessDto.isSuccess());
        verify(fundingRepository, times(1)).findById(any());
        assertEquals(expectDeadline, funding.getDeadline()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }


    @DisplayName("펀딩 종료하기-실패(펀딩이 존재하지 않음)")
    @Test
    void terminateFunding_NotFoundFunding() throws NoSuchFieldException, IllegalAccessException {
        //given
        Funding funding = FundingFixture.Graduate(member);
        when(fundingRepository.findById(funding.getFundingId())).thenReturn(Optional.empty());
        //when
        CommonException exception = assertThrows(CommonException.class, () ->
                fundingService.terminateFunding(funding.getFundingId()));
        //then
        assertEquals(NOT_FOUND_FUNDING.getMessage(), exception.getMessage());
    }

    @DisplayName("친구 펀딩 목록 조회-성공")
    @Test
    void getFriendFundingList_Success() throws NoSuchFieldException, IllegalAccessException {
        //given
        Member friend = MemberFixture.member2();
        List<Relationship> myRelationships = getMyRelationships(member, friend);
        Funding friendFunding = FundingFixture.Birthday(friend);

        List<Item> items = ItemFixture.items3();
        List<FundingItem> fundingItems = FundingItemFixture.fundingItems(items, friendFunding);

        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(myRelationships);
        when(fundingRepository.findByMemberIdAndStatus(friend.getMemberId(), true)).thenReturn(
                Optional.of(friendFunding));
        when(fundingItemRepository.findFundingItemIdListByFundingId(friendFunding.getFundingId())).thenReturn(
                fundingItems);
        //when
        List<CommonFriendFundingDto> friendFundingDtoList = fundingService.getFriendFundingList(member.getMemberId());
        CommonFriendFundingDto commonFriendFundingDto = friendFundingDtoList.get(0);
        //then
        assertNotNull(friendFundingDtoList);
        assertEquals(friendFunding.getFundingId(), commonFriendFundingDto.fundingId());
        assertEquals(friend.getNickName(), commonFriendFundingDto.nickName());
        assertEquals(items.get(0).getItemPrice(),
                commonFriendFundingDto.friendFundingPageItemDtoList().get(0).itemPrice());
    }

    @DisplayName("친구 펀딩 목록 조회-성공(친구가 없는 경우)")
    @Test
    void getFriendFundingList_NotFoundFriend() {
        //given
        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(anyList());
        //when
        List<CommonFriendFundingDto> friendFundingDtoList = fundingService.getFriendFundingList(member.getMemberId());
        //then
        assertEquals(List.of(), friendFundingDtoList);
    }

    @DisplayName("친구 펀딩 목록 조회-성공(친구의 펀딩이 없는 경우)")
    @Test
    void getFriendFundingList_NotFoundFriendFunding() throws NoSuchFieldException, IllegalAccessException {
        //given
        Member friend = MemberFixture.member2();
        List<Relationship> myRelationships = getMyRelationships(member, friend);

        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(myRelationships);
        when(fundingRepository.findByMemberIdAndStatus(friend.getMemberId(), true)).thenReturn(
                Optional.empty());
        //when
        List<CommonFriendFundingDto> friendFundingDtoList = fundingService.getFriendFundingList(member.getMemberId());

        //then
        assertEquals(List.of(), friendFundingDtoList);
    }

    @DisplayName("내 펀딩 이력 조회-성공")
    @Test
    void getMyFundingHistory_Success() throws NoSuchFieldException, IllegalAccessException {
        //given
        List<Item> itemList1 = ItemFixture.items3();
        List<Item> itemList2 = ItemFixture.items5();

        Funding funding1 = FundingFixture.Birthday(member);
        Funding funding2 = FundingFixture.terminatedFundingSuccess(member,
                itemList2.stream().mapToInt(Item::getItemPrice).sum());

        // 첫 번째 펀딩
        FundingItemFixture.fundingItems(itemList1, funding1);
        FundingItemFixture.fundingItems(itemList2, funding2);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberId(member.getMemberId())).thenReturn(List.of(funding1, funding2));
        when(contributorRepository.countContributorsForFunding(funding1.getFundingId())).thenReturn(1);

        //when
        MyFundingHistoryDto myFundingHistoryDto = fundingService.getMyFundingHistory(member.getMemberId());
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);
        MyPageFundingDetailHistoryDto myPageFundingDetailHistoryDto = MyPageFundingDetailHistoryDto.fromEntity(funding1,
                1, FundingUtils.calculateFundingPercent(funding1));
        //then
        assertEquals(myPageMemberDto, myFundingHistoryDto.myPageMemberDto());
        assertEquals(myPageFundingDetailHistoryDto, myFundingHistoryDto.myPageFundingDetailHistoryDtos().get(0));
    }

    @DisplayName("내 펀딩 이력 조회-성공(펀딩 이력 없음)")
    @Test
    void getMyFundingHistory_NotFoundFunding() {
        //given
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberId(member.getMemberId())).thenReturn(List.of());
        //when
        MyFundingHistoryDto myFundingHistoryDto = fundingService.getMyFundingHistory(member.getMemberId());
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);
        //then
        assertEquals(myPageMemberDto, myFundingHistoryDto.myPageMemberDto());
        assertEquals(0, myFundingHistoryDto.myPageFundingDetailHistoryDtos().size());
    }

    @DisplayName("내 펀딩 이력 조회-성공(펀딩 기여자 없음)")
    @Test
    void getMyFundingHistory_NotFoundContributor() throws NoSuchFieldException, IllegalAccessException {
        //given
        Funding funding = FundingFixture.Graduate(member);

        List<Item> itemList1 = ItemFixture.items3();
        FundingItemFixture.fundingItems(itemList1, funding);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingByMemberId(member.getMemberId())).thenReturn(List.of(funding));
        when(contributorRepository.countContributorsForFunding(funding.getFundingId())).thenReturn(0);
        //when
        MyFundingHistoryDto myFundingHistoryDto = fundingService.getMyFundingHistory(member.getMemberId());
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);
        MyPageFundingDetailHistoryDto myPageFundingDetailHistoryDto = MyPageFundingDetailHistoryDto.fromEntity(funding,
                0, FundingUtils.calculateFundingPercent(funding));
        //then
        assertEquals(myPageMemberDto, myFundingHistoryDto.myPageMemberDto());
        assertEquals(myPageFundingDetailHistoryDto, myFundingHistoryDto.myPageFundingDetailHistoryDtos().get(0));
    }

    @DisplayName("내 펀딩 이력 조회-실패(사용자를 찾을 수 없음)")
    @Test
    void getMyFundingHistory_NotFoundMember() {
        //given
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.empty());
        //when
        CommonException exception = assertThrows(CommonException.class, () ->
                fundingService.getMyFundingHistory(member.getMemberId()));
        //then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }

    static private List<Relationship> getMyRelationships(Member member, Member friend) {
        List<Relationship> relationships = Relationship.createRelationships(member, friend);
        List<Relationship> myRelationships = new ArrayList<>();
        myRelationships.add(relationships.get(0));
        return myRelationships;
    }
}