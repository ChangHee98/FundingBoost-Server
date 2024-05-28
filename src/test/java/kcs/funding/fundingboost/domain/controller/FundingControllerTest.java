package kcs.funding.fundingboost.domain.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.fundingRegist.RegisterFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.CommonFriendFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.FriendFundingPageItemDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.ContributorDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingDetailDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingItemDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeFriendFundingDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeItemDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeMemberInfoDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeMyFundingItemDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeMyFundingStatusDto;
import kcs.funding.fundingboost.domain.dto.response.home.HomeViewDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.friendFundingHistory.FriendFundingContributionDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.friendFundingHistory.FriendFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyFundingHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingHistory.MyPageFundingDetailHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.MyFundingHistoryDetailDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.MyFundingStatusDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.MyPageFundingItemDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.myFundingStatus.ParticipateFriendDto;
import kcs.funding.fundingboost.domain.entity.Contributor;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Relationship;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.FundingFixture;
import kcs.funding.fundingboost.domain.model.FundingItemFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.FundingService;
import kcs.funding.fundingboost.domain.service.utils.FundingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(FundingController.class)
public class FundingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundingService fundingService;

    @Autowired
    private ObjectMapper objectMapper;

    private Member member1;
    private Member member2;
    private Member member3;
    private Item item1;
    private Item item2;
    private Funding funding1;
    private Funding funding2;
    private FundingItem fundingItem1;
    private FundingItem fundingItem2;

    @BeforeEach
    public void setup() throws NoSuchFieldException, IllegalAccessException {
        member1 = MemberFixture.member1();
        member2 = MemberFixture.member2();
        member3 = MemberFixture.member3();
        item1 = ItemFixture.item1();
        item2 = ItemFixture.item2();
        funding1 = FundingFixture.Birthday(member1);
        funding2 = FundingFixture.BirthdayWithCollectPrice(member2, 1000);
        fundingItem1 = FundingItemFixture.fundingItem1(item1, funding1);
        fundingItem2 = FundingItemFixture.fundingItem1(item2, funding1);

        fundingItem1 = FundingItemFixture.fundingItem1(item1, funding2);
        fundingItem2 = FundingItemFixture.fundingItem1(item2, funding2);

        Relationship.createRelationships(member1, member2);
        SecurityContextHolderFixture.setContext(member1);
    }

    @DisplayName("메인페이지 조회")
    @Test
    void home() throws Exception {
        HomeMemberInfoDto homeMemberInfoDto = HomeMemberInfoDto.fromEntity(member1);
        Pageable pageable = Pageable.ofSize(10);

        // 내 펀딩 아이템
        List<HomeMyFundingItemDto> homeMyFundingItemDtoList = List.of(
                HomeMyFundingItemDto.fromEntity(fundingItem1, 100), HomeMyFundingItemDto.fromEntity(fundingItem2, 50));

        // 친구 펀딩 아이템
        List<HomeFriendFundingDto> homeFriendFundingDtoList = Collections.singletonList(
                HomeFriendFundingDto.fromEntity(
                        CommonFriendFundingDto.fromEntity(funding2, "D-7", 80, new ArrayList<>()), null));

        // 아이템
        List<HomeItemDto> itemDtoList = List.of(HomeItemDto.fromEntity(item1), HomeItemDto.fromEntity(item2));

        int percent = funding1.getCollectPrice() * 100 / funding1.getTotalPrice();

        HomeMyFundingStatusDto homeMyFundingStatusDto = HomeMyFundingStatusDto.fromEntity(funding1, "D-3", percent,
                homeMyFundingItemDtoList);

        HomeViewDto homeViewDto = HomeViewDto.fromEntity(homeMemberInfoDto, homeMyFundingStatusDto,
                homeFriendFundingDtoList, itemDtoList);

//        given(fundingService.getMainView(member1.getMemberId(), pageable, anyLong())).willReturn(homeViewDto);
        given(fundingService.getMainView(member1.getMemberId(), pageable, 11L)).willReturn(homeViewDto);

        mockMvc.perform(get("/api/v1/home")
                        .param("memberId", "1")
                        .param("lastItemId", "11")// Adjust parameters as needed
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.homeMemberInfoDto.nickName")
                        .value("임창희"))
                .andExpect(jsonPath("$.data.homeMemberInfoDto.profile")
                        .value(member1.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.homeMyFundingStatusDto.homeMyFundingItemDtoList.length()")
                        .value(2))
                .andExpect(jsonPath("$.data.homeMyFundingStatusDto.homeMyFundingItemDtoList[0].itemImageUrl")
                        .value(item1.getItemImageUrl()))
                .andExpect(jsonPath("$.data.homeMyFundingStatusDto.homeMyFundingItemDtoList[0].itemPercent")
                        .value(100))
                .andExpect(jsonPath("$.data.homeMyFundingStatusDto.deadline")
                        .value("D-3"))
                .andExpect(jsonPath("$.data.homeFriendFundingDtoList.length()")
                        .value(1))
                .andExpect(jsonPath("$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.fundingId")
                        .value(2L))
                .andExpect(jsonPath("$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.nickName")
                        .value(member2.getNickName()))
                .andExpect(jsonPath("$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.friendProfileImgUrl")
                        .value(member2.getProfileImgUrl()))
                .andExpect(jsonPath(
                        "$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.friendFundingDeadlineDate")
                        .value("D-7"))
                .andExpect(jsonPath(
                        "$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.friendFundingPercent")
                        .value(80))
                .andExpect(jsonPath(
                        "$.data.homeFriendFundingDtoList[0].commonFriendFundingDto.friendFundingPageItemDtoList.length()")
                        .value(0))
                .andExpect(jsonPath("$.data.itemDtoList.length()")
                        .value(2))
                .andExpect(jsonPath("$.data.itemDtoList[0].itemId")
                        .value(1))
                .andExpect(jsonPath("$.data.itemDtoList[0].itemName")
                        .value(item1.getItemName()))
                .andExpect(jsonPath("$.data.itemDtoList[0].price")
                        .value(item1.getItemPrice()))
                .andExpect(jsonPath("$.data.itemDtoList[0].itemImageUrl")
                        .value(item1.getItemImageUrl()))
                .andExpect(jsonPath("$.data.itemDtoList[0].brandName")
                        .value(item1.getBrandName()));
    }

    @DisplayName("펀딩 등록하기")
    @Test
    void registerFunding() throws Exception {
        RegisterFundingDto registerFundingDto = new RegisterFundingDto(List.of(1L, 2L), "생일", "생일",
                LocalDate.of(2024, 4, 30));
        CommonSuccessDto expectedResponse = CommonSuccessDto.fromEntity(true);

        when(fundingService.putFundingAndFundingItem(anyLong(), any(RegisterFundingDto.class)))
                .thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/funding")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerFundingDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("펀딩 종료하기")
    @Test
    void closeFunding() throws Exception {
        CommonSuccessDto expectedResponse = CommonSuccessDto.fromEntity(true);

        when(fundingService.terminateFunding(funding1.getFundingId())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/funding/close/{fundingId}", funding1.getFundingId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("친구 펀딩 디테일 페이지 조회")
    @Test
    void viewFriendsFundingDetail() throws Exception {
        List<FriendFundingItemDto> friendFundingItemList = List.of(
                FriendFundingItemDto.fromEntity(fundingItem1), FriendFundingItemDto.fromEntity(fundingItem2));
        List<ContributorDto> contributorList = List.of(
                ContributorDto.fromEntity(Contributor.createContributor(20000, member3, funding1)));

        FriendFundingDetailDto
                friendFundingDetailDto = FriendFundingDetailDto.fromEntity(friendFundingItemList, funding1,
                contributorList, 80);
        when(fundingService.viewFriendsFundingDetail(funding1.getFundingId(), member1.getMemberId())).thenReturn(
                friendFundingDetailDto);

        mockMvc.perform(get("/api/v1/funding/friends/{fundingId}", funding1.getFundingId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.friendFundingItemList.length()").value(2))
                .andExpect(jsonPath("$.data.friendFundingItemList[0].itemImageUrl").value(
                        item1.getItemImageUrl()))
                .andExpect(jsonPath("$.data.friendFundingItemList[0].itemId").value(item1.getItemId()))
                .andExpect(jsonPath("$.data.friendFundingItemList[0].itemName").value(item1.getItemName()))
                .andExpect(jsonPath("$.data.friendFundingItemList[0].optionName").value(item1.getOptionName()))
                .andExpect(jsonPath("$.data.friendFundingItemList[0].itemPrice").value(item1.getItemPrice()));
    }

    @DisplayName("친구 펀딩 목록 조회")
    @Test
    void viewFriendFundingList() throws Exception {
        CommonFriendFundingDto commonFriendFundingDto = CommonFriendFundingDto.fromEntity(funding2, "D-3",
                80, List.of(
                        FriendFundingPageItemDto.fromEntity(item1),
                        FriendFundingPageItemDto.fromEntity(item2)));

        List<CommonFriendFundingDto> commonFriendFundingDtoList = List.of(commonFriendFundingDto);

        List<FundingItem> fundingItems = funding2.getFundingItems();
        FundingItem fundingItem = fundingItems.get(0);
        Item item = fundingItem.getItem();

        when(fundingService.getFriendFundingList(member1.getMemberId())).thenReturn(commonFriendFundingDtoList);

        // 실행 및 검증
        mockMvc.perform(get("/api/v1/funding/friends")
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data[0].fundingId")
                        .value(funding2.getFundingId()))
                .andExpect(jsonPath("$.data[0].nickName")
                        .value(member2.getNickName()))
                .andExpect(jsonPath("$.data[0].friendProfileImgUrl")
                        .value(member2.getProfileImgUrl()))
                .andExpect(jsonPath("$.data[0].friendFundingDeadlineDate")
                        .value("D-3"))
                .andExpect(jsonPath("$.data[0].tag")
                        .value(funding2.getTag().getDisplayName()))
                .andExpect(jsonPath("$.data[0].collectPrice")
                        .value(funding2.getCollectPrice()))
                .andExpect(jsonPath("$.data[0].friendFundingPercent")
                        .value(80))
                .andExpect(jsonPath("$.data[0].friendFundingPageItemDtoList.length()")
                        .value(fundingItems.size()))
                .andExpect(jsonPath("$.data[0].friendFundingPageItemDtoList[0].itemPrice")
                        .value(item.getItemPrice()))
                .andExpect(jsonPath("$.data[0].friendFundingPageItemDtoList[0].itemImageUrl")
                        .value(item.getItemImageUrl()));
    }

    @DisplayName("펀딩 기간 늘리기")
    @Test
    void extendMyFunding() throws Exception {
        CommonSuccessDto expectedResponse = CommonSuccessDto.fromEntity(true);

        when(fundingService.extendFunding(funding1.getFundingId())).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/v1/funding/extension/{fundingId}", funding1.getFundingId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.isSuccess").value(true));
    }

    @DisplayName("마이 페이지 조회")
    @Test
    void viewMyPage() throws Exception {
        List<MyPageFundingItemDto> myPageFundingItemList = List.of(
                MyPageFundingItemDto.fromEntity(funding1, fundingItem1, 80));
        List<ParticipateFriendDto> participateFriendDtoList = List.of(
                ParticipateFriendDto.fromEntity(Contributor.createContributor(20000, member2, funding1)));

        MyFundingStatusDto myFundingStatusDto = MyFundingStatusDto.createMyFundingStatusDto(
                MyPageMemberDto.fromEntity(member1),
                myPageFundingItemList,
                participateFriendDtoList,
                80,
                "2024-05-02",
                "D-3",
                funding1.getTag().getDisplayName(),
                funding1.getMessage()
        );

        given(fundingService.getMyFundingStatus(member1.getMemberId())).willReturn(myFundingStatusDto);

        mockMvc.perform(get("/api/v1/funding/my-funding-status")
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName").value(member1.getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email").value(member1.getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl").value(member1.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.myPageMemberDto.point").value(member1.getPoint()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList.length()").value(1))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].fundingId").value(funding1.getFundingId()))
                .andExpect(
                        jsonPath("$.data.myPageFundingItemDtoList[0].itemName").value(item1.getItemName()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemPrice").value(item1.getItemPrice()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemImageUrl").value(item1.getItemImageUrl()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].optionName").value(item1.getOptionName()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemPercent").value(80))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].finishedStatus").value(true))
                .andExpect(jsonPath("$.data.participateFriendDtoList.length()").value(1))
                .andExpect(
                        jsonPath("$.data.participateFriendDtoList[0].participateNickname").value(member2.getNickName()))
                .andExpect(jsonPath("$.data.participateFriendDtoList[0].participatePrice").value(20000))
                .andExpect(jsonPath("$.data.participateFriendDtoList[0].participateProfileImgUrl")
                        .value(member2.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.totalPercent").value(80))
                .andExpect(jsonPath("$.data.deadline").value("2024-05-02"))
                .andExpect(jsonPath("$.data.deadlineDate").value("D-3"));
    }

    @DisplayName("지난 펀딩 이력 조회")
    @Test
    void viewMyFundingHistory() throws Exception {
        List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtoList = List.of(
                MyPageFundingDetailHistoryDto.fromEntity(funding1, 2, FundingUtils.calculateFundingPercent(funding1)));

        MyFundingHistoryDto myFundingHistoryDto = MyFundingHistoryDto.fromEntity(MyPageMemberDto.fromEntity(member1),
                myPageFundingDetailHistoryDtoList);

        Item item = funding1.getFundingItems().get(0).getItem();

        given(fundingService.getMyFundingHistory(member1.getMemberId())).willReturn(myFundingHistoryDto);

        mockMvc.perform(get("/api/v1/funding/history")
                        .param("memberId", member1.getMemberId().toString())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success")
                        .value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName")
                        .value(member1.getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email")
                        .value(member1.getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl")
                        .value(member1.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos.length()")
                        .value(1))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].fundingId")
                        .value(1))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].createdDate")
                        .value(funding1.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].deadLine")
                        .value(funding1.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].itemImageUrl")
                        .value(item.getItemImageUrl()))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].optionName")
                        .value(item.getOptionName()))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].status")
                        .value(funding1.isFundingStatus()))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].contributorCount")
                        .value(2))
                .andExpect(jsonPath("$.data.myPageFundingDetailHistoryDtos[0].tag")
                        .value(funding1.getTag().getDisplayName()));
    }

    @DisplayName("지난 펀딩 이력 상세 조회")
    @Test
    void viewMyFundingHistoryDetail() throws Exception {
        // given
        List<MyPageFundingItemDto> myPageFundingItemDtoList = List.of(
                MyPageFundingItemDto.fromEntity(funding1, fundingItem1, 80),
                MyPageFundingItemDto.fromEntity(funding1, fundingItem2, 0));
        List<ParticipateFriendDto> participateFriendDtoList = List.of(
                ParticipateFriendDto.fromEntity(Contributor.createContributor(20000, member2, funding1)));

        List<FundingItem> fundingItems = funding1.getFundingItems();
        FundingItem fundingItem = fundingItems.get(0);
        Item item = fundingItem.getItem();

        MyFundingHistoryDetailDto myFundingHistoryDetailDto = MyFundingHistoryDetailDto.createMyFundingHistoryDetailDto(
                MyPageMemberDto.fromEntity(member1), myPageFundingItemDtoList, participateFriendDtoList,
                90, "2024-05-02", "2024-05-16");

        Mockito.when(fundingService.getMyFundingHistoryDetails(funding1.getFundingId()))
                .thenReturn(myFundingHistoryDetailDto);

        // then
        mockMvc.perform(get("/api/v1/funding/history/{fundingId}", funding1.getFundingId())
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName")
                        .value(member1.getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email")
                        .value(member1.getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl")
                        .value(member1.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList.length()")
                        .value(fundingItems.size()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].fundingId")
                        .value(funding1.getFundingId()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemName")
                        .value(item.getItemName()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemPrice")
                        .value(item.getItemPrice()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemImageUrl")
                        .value(item.getItemImageUrl()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].optionName")
                        .value(item.getOptionName()))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].itemPercent")
                        .value(80))
                .andExpect(jsonPath("$.data.myPageFundingItemDtoList[0].finishedStatus")
                        .value(funding1.isFundingStatus()))
                .andExpect(jsonPath("$.data.participateFriendDtoList.length()")
                        .value(1))
                .andExpect(jsonPath("$.data.participateFriendDtoList[0].participateNickname")
                        .value(member2.getNickName()))
                .andExpect(jsonPath("$.data.participateFriendDtoList[0].participatePrice")
                        .value(20000))
                .andExpect(jsonPath("$.data.participateFriendDtoList[0].participateProfileImgUrl")
                        .value(member2.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.totalPercent").value(90))
                .andExpect(jsonPath("$.data.createdDate").value("2024-05-02"))
                .andExpect(jsonPath("$.data.deadline").value("2024-05-16"));
    }

    @DisplayName("친구 펀딩 이력 조회")
    @Test
    void viewFriendFundingHistory() throws Exception {
        List<FriendFundingContributionDto> friendFundingContributionDto = List.of(
                FriendFundingContributionDto.fromEntity(
                        Contributor.createContributor(20000, member2, funding2), funding2));

        FriendFundingHistoryDto friendFundingHistoryDto = FriendFundingHistoryDto.fromEntity(
                MyPageMemberDto.fromEntity(member1), friendFundingContributionDto);

        given(fundingService.getFriendFundingHistory(member1.getMemberId())).willReturn(friendFundingHistoryDto);

        mockMvc.perform(get("/api/v1/funding/history/friend")
                        .contentType(APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName")
                        .value(member1.getNickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email")
                        .value(member1.getEmail()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl")
                        .value(member1.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.myPageMemberDto.point")
                        .value(member1.getPoint()))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto.length()")
                        .value(1))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto[0].nickname")
                        .value(member2.getNickName()))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto[0].price")
                        .value(20000))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto[0].friendProfileImg")
                        .value(member2.getProfileImgUrl()))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto[0].tag")
                        .value(funding1.getTag().getDisplayName()))
                .andExpect(jsonPath("$.data.FriendFundingContributionDto[0].createdDate")
                        .value(funding1.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
    }
}