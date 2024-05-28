package kcs.funding.fundingboost.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.response.home.HomeViewDto;
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
import kcs.funding.fundingboost.domain.model.RelationShipFixture;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import kcs.funding.fundingboost.domain.repository.fundingItem.FundingItemRepository;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import kcs.funding.fundingboost.domain.repository.relationship.RelationshipRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@Slf4j
@ExtendWith(MockitoExtension.class)
class FundingServiceTest_InHo_v2 {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private FundingRepository fundingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private RelationshipRepository relationshipRepository;

    @Mock
    private FundingItemRepository fundingItemRepository;

    @InjectMocks
    private FundingService fundingService;


    private Member member;
    private Item item1;
    private Item item2;
    private Funding funding;
    private Pageable pageable;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member2();
        funding = FundingFixture.Birthday(member);
        item1 = ItemFixture.item1();
        item2 = ItemFixture.item2();

        pageable = Pageable.ofSize(10);
    }

    @DisplayName("getMainView: 로그인이 되지 않았을 시 예외처리가 되어야한다.")
    @Test
    void getMainView_잘못된사용자() {
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);
        //given
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.empty());

        //when & then
        Assertions.assertThatThrownBy(() -> fundingService.getMainView(member.getMemberId(), pageable, 11L))
                .isInstanceOf(CommonException.class)
                .hasMessageContaining("해당 사용자가 존재하지 않습니다.");
    }

    @DisplayName("getMainView: 로그인시 내 펀딩목록 조회")
    @Test
    void getMainView_내펀딩목록조회() throws NoSuchFieldException, IllegalAccessException {

        //given
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);
        List<FundingItem> fundigItems = FundingItemFixture.fundingItems(List.of(item1, item2), funding);
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingInfo(member.getMemberId())).thenReturn(Optional.of(funding));

        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);

        //then
        assertThat(result.homeMemberInfoDto().nickName()).isEqualTo(member.getNickName());
        assertThat(result.homeMyFundingStatusDto().deadline()).isEqualTo("D-15");
        assertThat(result.homeMyFundingStatusDto().totalPercent()).isEqualTo(0);
        assertThat(result.homeMyFundingStatusDto().homeMyFundingItemDtoList()).extracting("itemPercent")
                .contains(0, 0);

    }

    @Test
    @DisplayName("getMainView: 사용자의 펀딩이 존재하지 않을 때 빈 리스트가 와야한다.")
    void getMainView_내펀딩이존재하지않음() {
        //given
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingInfo(member.getMemberId())).thenReturn(Optional.empty());

        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);
        //then
        assertThat(result.homeMemberInfoDto().nickName()).isEqualTo(member.getNickName());
        assertThat(result.homeMyFundingStatusDto()).isNull();
    }

    @DisplayName("getMainView: 사용자의 친구가 진행중인 펀딩현황 조회, 진행중인 펀딩이 없는 친구는 조회가 되지 않아야 한다.")
    @Test
    void getMainView_친구펀딩현황조회() throws NoSuchFieldException, IllegalAccessException {
        //given
        /**
         * 친구 맴버 생성
         */
        List<Member> friendList = new ArrayList<>();
        Member friend1 = MemberFixture.member1();
        Member friend2 = MemberFixture.member2();
        friendList.add(friend1);
        friendList.add(friend2);

        /**
         * 한 명의 친구만 펀딩 및 펀딩 아이템 생성
         */
        Funding friendFunding = FundingFixture.BirthdayWithCollectPrice(friend1, 86500);

        FundingItem friendFundingItem1 = FundingItemFixture.fundingItem1(ItemFixture.item1(), friendFunding);
        FundingItem friendFundingItem2 = FundingItemFixture.fundingItem2(ItemFixture.item2(), friendFunding);
        FundingItem friendFundingItem3 = FundingItemFixture.fundingItem3(ItemFixture.item3(), friendFunding);

        List<FundingItem> friendFundingItems = List.of(friendFundingItem1, friendFundingItem2, friendFundingItem3);

        /**
         * 친구 관계 생성
         */
        List<Relationship> relationships = RelationShipFixture.myRelationships(member, friendList);
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(relationships);
        when(fundingRepository.findByMemberIdAndStatus(relationships.get(0).getFriend().getMemberId(), true))
                .thenReturn(Optional.of(friendFunding));
        when(fundingRepository.findByMemberIdAndStatus(relationships.get(1).getFriend().getMemberId(), true))
                .thenReturn(Optional.empty());

        when(fundingItemRepository.findFundingItemIdListByFundingId(friendFunding.getFundingId()))
                .thenReturn(friendFundingItems);
        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);
        //then
        assertThat(result.homeFriendFundingDtoList()).hasSize(1);
        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingDeadlineDate()).isEqualTo("D-7");
        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).hasSize(3);
        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto().collectPrice()).isEqualTo(86500);
        assertThat(result.homeFriendFundingDtoList().get(0).nowFundingItemImageUrl())
                .isEqualTo(
                        "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg");
        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto().friendFundingPercent()).isEqualTo(
                43);
        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).extracting("itemPrice").contains(61000, 51000, 85000);
    }

    @DisplayName("getMainView: 친구가 없는 경우 빈 리스트를 가져와야 한다.")
    @Test
    void getMainView_친구가존재하지않음() {
        //given
        List<Relationship> relationships = new ArrayList<>();
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(relationships);
        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);
        //then
        assertThat(result.homeFriendFundingDtoList()).isEmpty();
    }

    @DisplayName("getMainView: 상품 조회")
    @Test
    void getMainView_상품조회() throws NoSuchFieldException, IllegalAccessException {
        //given
        Slice<Item> itemSlice = new SliceImpl<>(List.of(item1, item2), pageable, false);

        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));

        //when

        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);

        //then

        assertThat(result.itemDtoList()).hasSize(2);
        assertThat(result.itemDtoList()).extracting("itemId").contains(1L, 2L);
    }

    @DisplayName("getMainView: 상품이 없는 경우 빈 리스트를 반환해야한다.")
    @Test
    void getMainView_상품이없는경우() {
        //given
        Slice<Item> itemList = new SliceImpl<>(List.of(), pageable, false);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemList);

        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);

        //then
        assertThat(result.itemDtoList()).isEmpty();
    }

    @DisplayName("getMainView: 사용자 펀딩과 친구 펀딩, 상품 목록 모두 조회")
    @Test
    void getMainView() throws NoSuchFieldException, IllegalAccessException {
        //given

        /**
         * 사용자 펀딩 생성
         */
        List<Item> itemList = ItemFixture.items5();
        Slice<Item> itemSlice = new SliceImpl<>(itemList, pageable, false);
        List<FundingItem> fundigItems = FundingItemFixture.fundingItems(itemList, funding);

        /**
         * 친구 맴버 생성
         */
        List<Member> friendList = new ArrayList<>();
        Member friend1 = MemberFixture.member1();
        Member friend2 = MemberFixture.member2();
        friendList.add(friend1);
        friendList.add(friend2);

        /**
         * 모든 친구 펀딩 및 펀딩 아이템 생성
         */
        Funding friendFunding1 = FundingFixture.BirthdayWithCollectPrice(friend1, 86500);
        Funding friendFunding2 = FundingFixture.Graduate(friend2);

        FundingItem friend1FundingItem1 = FundingItemFixture.fundingItem1(ItemFixture.item1(), friendFunding1);
        FundingItem friend1FundingItem2 = FundingItemFixture.fundingItem2(ItemFixture.item2(), friendFunding1);
        FundingItem friend1FundingItem3 = FundingItemFixture.fundingItem3(ItemFixture.item3(), friendFunding1);

        FundingItem friend2FundingItem3 = FundingItemFixture.fundingItem3(ItemFixture.item3(), friendFunding2);
        FundingItem friend2FundingItem4 = FundingItemFixture.fundingItem3(ItemFixture.item4(), friendFunding2);

        List<FundingItem> friend1FundingItems = List.of(friend1FundingItem1, friend1FundingItem2, friend1FundingItem3);
        List<FundingItem> friend2FundingItems = List.of(friend2FundingItem3, friend2FundingItem4);
        /**
         * 친구 관계 생성
         */
        List<Relationship> relationships = RelationShipFixture.myRelationships(member, friendList);

        /**
         * 사용자 펀딩 관련 repository 접근
         */
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(fundingRepository.findFundingInfo(member.getMemberId())).thenReturn(Optional.ofNullable(funding));

        /**
         * 친구 펀딩 관련 repository 접근
         */
        when(relationshipRepository.findFriendByMemberId(member.getMemberId())).thenReturn(relationships);
        when(fundingRepository.findByMemberIdAndStatus(relationships.get(0).getFriend().getMemberId(), true))
                .thenReturn(Optional.of(friendFunding1));
        when(fundingRepository.findByMemberIdAndStatus(relationships.get(1).getFriend().getMemberId(), true))
                .thenReturn(Optional.of(friendFunding2));
        when(fundingItemRepository.findFundingItemIdListByFundingId(friendFunding1.getFundingId()))
                .thenReturn(friend1FundingItems);
        when(fundingItemRepository.findFundingItemIdListByFundingId(friendFunding2.getFundingId()))
                .thenReturn(friend2FundingItems);

        /**
         * 상품 관련 repository 접근
         */
        when(itemRepository.findItemsBySlice(11L, pageable)).thenReturn(itemSlice);

        //when
        HomeViewDto result = fundingService.getMainView(member.getMemberId(), pageable, 11L);

        //then
        /**
         * 사용자 관련 테스트
         */
        assertThat(result.homeMemberInfoDto().nickName()).isEqualTo(member.getNickName());
        assertThat(result.homeMyFundingStatusDto().deadline()).isEqualTo("D-15");
        assertThat(result.homeMyFundingStatusDto().totalPercent()).isEqualTo(0);
        assertThat(result.homeMyFundingStatusDto().homeMyFundingItemDtoList()).extracting("itemPercent")
                .contains(0, 0);

        /**
         * 친구 펀딩 관련 테스트
         */
        assertThat(result.homeFriendFundingDtoList()).hasSize(2);

        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingDeadlineDate()).isEqualTo("D-7");
        assertThat(result.homeFriendFundingDtoList().get(1).commonFriendFundingDto()
                .friendFundingDeadlineDate()).isEqualTo("D-14");

        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).hasSize(3);
        assertThat(result.homeFriendFundingDtoList().get(1).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).hasSize(2);

        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto().collectPrice()).isEqualTo(86500);
        assertThat(result.homeFriendFundingDtoList().get(1).commonFriendFundingDto().collectPrice()).isEqualTo(0);

        assertThat(result.homeFriendFundingDtoList().get(0).nowFundingItemImageUrl())
                .isEqualTo(
                        "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg");
        assertThat(result.homeFriendFundingDtoList().get(1).nowFundingItemImageUrl())
                .isEqualTo(
                        "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg");

        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto().friendFundingPercent()).isEqualTo(
                43);
        assertThat(result.homeFriendFundingDtoList().get(1).commonFriendFundingDto().friendFundingPercent()).isEqualTo(
                0);

        assertThat(result.homeFriendFundingDtoList().get(0).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).extracting("itemPrice").contains(61000, 51000, 85000);
        assertThat(result.homeFriendFundingDtoList().get(1).commonFriendFundingDto()
                .friendFundingPageItemDtoList()).extracting("itemPrice").contains(85000);

        /**
         * 아이템 관련 테스트
         */
        assertThat(result.itemDtoList()).extracting("itemId").contains(1L, 2L, 3L, 4L, 5L);
    }

}