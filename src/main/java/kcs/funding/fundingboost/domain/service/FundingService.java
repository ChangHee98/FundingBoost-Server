package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_ACCESS_URL;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_FUNDING_STATUS;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.fundingRegist.RegisterFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.CommonFriendFundingDto;
import kcs.funding.fundingboost.domain.dto.response.common.FriendFundingPageItemDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.ContributorDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingDetailDto;
import kcs.funding.fundingboost.domain.dto.response.friendFundingDetail.FriendFundingItemDto;
import kcs.funding.fundingboost.domain.dto.response.fundingRegist.FundingRegisterStatusDto;
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
import kcs.funding.fundingboost.domain.entity.Tag;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.contributor.ContributorRepository;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import kcs.funding.fundingboost.domain.repository.fundingItem.FundingItemRepository;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import kcs.funding.fundingboost.domain.repository.relationship.RelationshipRepository;
import kcs.funding.fundingboost.domain.service.utils.DateUtils;
import kcs.funding.fundingboost.domain.service.utils.FundingConst;
import kcs.funding.fundingboost.domain.service.utils.FundingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FundingService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final FundingRepository fundingRepository;
    private final FundingItemRepository fundingItemRepository;
    private final ContributorRepository contributorRepository;
    private final RelationshipRepository relationshipRepository;

    @Transactional
    public CommonSuccessDto putFundingAndFundingItem(Long memberId, RegisterFundingDto registerFundingDto) {

        List<Long> registerFundingItemList = registerFundingDto.itemIdList();

        List<Item> itemList = registerFundingItemList.stream()
                .map(itemIdList -> itemRepository.findById(itemIdList)
                        .orElseThrow(() -> new CommonException(NOT_FOUND_ITEM))).toList();

        if (itemList.isEmpty()) {
            throw new CommonException(INVALID_FUNDING_STATUS);
        }

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        Funding funding = Funding.createFunding(
                member,
                registerFundingDto.fundingMessage(),
                Tag.getTag(registerFundingDto.tag()),
                registerFundingDto.deadline().atTime(23, 59, 59));

        fundingRepository.save(funding);

        List<FundingItem> fundingItemList = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i++) {
            fundingItemList.add(FundingItem.createFundingItem(funding, itemList.get(i), i));
        }
        fundingItemRepository.saveAll(fundingItemList);

        return CommonSuccessDto.fromEntity(true);
    }

    @Transactional
    public CommonSuccessDto terminateFunding(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_FUNDING));
        funding.terminate();
        return CommonSuccessDto.fromEntity(true);
    }

    public FriendFundingDetailDto viewFriendsFundingDetail(Long fundingId, Long memberId) {
        List<FundingItem> fundingItems = fundingItemRepository.findAllByFundingId(fundingId);

        if (!fundingItems.isEmpty()) {
            List<FriendFundingItemDto> friendFundingItemList = fundingItems.stream()
                    .map(FriendFundingItemDto::fromEntity)
                    .toList();
            Funding funding = fundingItems.get(0).getFunding();

            if (funding.getMember().getMemberId().equals(memberId)) {
                throw new CommonException(INVALID_ACCESS_URL);
            }

            List<ContributorDto> contributorList = contributorRepository.findByFundingId(fundingId)
                    .stream()
                    .map(ContributorDto::fromEntity)
                    .toList();

            int contributedPercent = FundingUtils.calculateFundingPercent(funding);

            return FriendFundingDetailDto.fromEntity(friendFundingItemList, funding, contributorList,
                    contributedPercent);
        } else {
            throw new CommonException(NOT_FOUND_FUNDING);
        }
    }

    private List<CommonFriendFundingDto> getCommonFriendFundingList(Long memberId) {
        List<CommonFriendFundingDto> commonFriendFundingDtoList = new ArrayList<>();
        List<Relationship> relationshipList = relationshipRepository.findFriendByMemberId(memberId);
        for (Relationship relationship : relationshipList) {
            Optional<Funding> findFriendFunding = fundingRepository.findByMemberIdAndStatus(
                    relationship.getFriend().getMemberId(), true);

            if (findFriendFunding.isEmpty()) {
                continue;
            }

            Funding friendFunding = findFriendFunding.get();
            String deadline = DateUtils.toDeadlineString(friendFunding);

            List<FundingItem> fundingItemList = fundingItemRepository.findFundingItemIdListByFundingId(
                    friendFunding.getFundingId());
            List<FriendFundingPageItemDto> friendFundingPageItemDtoList = fundingItemList.stream()
                    .map(fundingItem -> FriendFundingPageItemDto.fromEntity(fundingItem.getItem())).toList();

            int fundingTotalPercent = FundingUtils.calculateFundingPercent(friendFunding);
            commonFriendFundingDtoList.add(CommonFriendFundingDto.fromEntity(
                    friendFunding,
                    deadline,
                    fundingTotalPercent,
                    friendFundingPageItemDtoList
            ));
        }

        return commonFriendFundingDtoList;
    }

    public List<CommonFriendFundingDto> getFriendFundingList(Long memberId) {
        return getCommonFriendFundingList(memberId);
    }

    @Transactional
    public CommonSuccessDto extendFunding(Long fundingId) {
        Funding funding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_FUNDING));
        funding.extendDeadline(FundingConst.EXTEND_DEADLINE);
        return CommonSuccessDto.fromEntity(true);
    }

    public HomeViewDto getMainView(Long memberId, Pageable pageable, Long lastItemId) {

        // 상품 목록: 상품Id, 이름, 가격, 이미지, 브랜드명
        List<HomeItemDto> itemList = itemRepository.findItemsBySlice(lastItemId, pageable).stream()
                .map(HomeItemDto::fromEntity)
                .toList();
        // 로그인하지 않은 사용자가 home 조회시 ItemList만 출력
        if (memberId == null) {
            return HomeViewDto.fromEntity(null, null, null, itemList);
        }
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        Optional<Funding> funding = fundingRepository.findFundingInfo(memberId);
        // 사용자 정보: 이름, 프로필 이미지
        HomeMemberInfoDto homeMemberInfoDto = HomeMemberInfoDto.fromEntity(member);

        // 사용자 펀딩 내용: 펀딩 이름, 완료일, 내 펀딩 아이템들
        HomeMyFundingStatusDto myFundingStatus = null;
        if (funding.isPresent()) {
            myFundingStatus = getMyFundingStatus(funding.get());
        }

        // 친구 펀딩: 이름, 프로필 이미지, 펀딩Id, 현재 펀딩 진행중인 상품 이미지, 펀딩 진행률, 펀딩 마감일
        List<HomeFriendFundingDto> homeFriendFundingList = getFriendFundingListByHome(memberId);

        return HomeViewDto.fromEntity(homeMemberInfoDto, myFundingStatus, homeFriendFundingList, itemList);
    }

    private List<HomeFriendFundingDto> getFriendFundingListByHome(Long memberId) {

        List<CommonFriendFundingDto> commonFriendFundingDtoList = getCommonFriendFundingList(memberId);
        List<HomeFriendFundingDto> friendFundingDtoList = new ArrayList<>();

        for (CommonFriendFundingDto commonFriendFundingDto : commonFriendFundingDtoList) {
            int collectPrice = commonFriendFundingDto.collectPrice();
            String nowFundingItemImageUrl = null;
            for (FriendFundingPageItemDto friendFundingPageItemDto : commonFriendFundingDto.friendFundingPageItemDtoList()) {
                int itemPrice = friendFundingPageItemDto.itemPrice();

                if (collectPrice >= itemPrice) {
                    collectPrice -= itemPrice;
                } else {
                    nowFundingItemImageUrl = friendFundingPageItemDto.itemImageUrl();
                    break;
                }
            }
            friendFundingDtoList.add(HomeFriendFundingDto.fromEntity(
                    commonFriendFundingDto,
                    nowFundingItemImageUrl));
        }
        return friendFundingDtoList;
    }

    private List<HomeMyFundingItemDto> getMyFundingItems(Funding funding) {
        int collectPrice = funding.getCollectPrice();
        List<FundingItem> myFundingItems = funding.getFundingItems();
        List<HomeMyFundingItemDto> myFundingItemDtoList = new ArrayList<>();
        for (FundingItem myFundingItem : myFundingItems) {
            int itemPercent;
            if (collectPrice >= myFundingItem.getItem().getItemPrice()) {
                collectPrice -= myFundingItem.getItem().getItemPrice();
                itemPercent = 100;
            } else if (collectPrice > 0) {
                itemPercent = collectPrice * 100 / myFundingItem.getItem().getItemPrice();
                collectPrice = 0;
            } else {
                itemPercent = 0;
            }
            HomeMyFundingItemDto homeMyFundingItemDto = HomeMyFundingItemDto.fromEntity(
                    myFundingItem, itemPercent);
            myFundingItemDtoList.add(homeMyFundingItemDto);
        }
        return myFundingItemDtoList;
    }

    private HomeMyFundingStatusDto getMyFundingStatus(Funding funding) {
        String deadline = DateUtils.toDeadlineString(funding);
        // 사용자 펀딩 상세: 펀딩 상품 이미지, 펀딩 진행률
        List<HomeMyFundingItemDto> homeMyFundingItemList = getMyFundingItems(funding);

        int totalPercent = FundingUtils.calculateFundingPercent(funding);

        return HomeMyFundingStatusDto.fromEntity(funding, deadline, totalPercent, homeMyFundingItemList);
    }

    public MyFundingStatusDto getMyFundingStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        Optional<Funding> findFunding = fundingRepository.findByMemberIdAndStatus(member.getMemberId(), true);
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);
        if (findFunding.isEmpty()) {
            return MyFundingStatusDto.createNotExistFundingMyFundingStatusDto(myPageMemberDto);
        } else {
            Funding funding = findFunding.get();
            List<MyPageFundingItemDto> myPageFundingItemList = getMyPageFundingItemDtoList(funding);
            List<ParticipateFriendDto> participateFriendDtoList = getParticipateFriendDtoList(funding);

            int totalPercent = FundingUtils.calculateFundingPercent(funding);
            String deadlineDate = DateUtils.toDeadlineString(funding);

            return MyFundingStatusDto.createMyFundingStatusDto(
                    myPageMemberDto,
                    myPageFundingItemList,
                    participateFriendDtoList,
                    totalPercent,
                    funding.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    deadlineDate,
                    funding.getTag().getDisplayName(),
                    funding.getMessage()
            );
        }
    }

    private List<ParticipateFriendDto> getParticipateFriendDtoList(Funding funding) {
        List<Contributor> contributorList = contributorRepository.findAllByFundingId(funding.getFundingId());
        return contributorList.stream()
                .map(ParticipateFriendDto::fromEntity).toList();
    }

    private List<MyPageFundingItemDto> getMyPageFundingItemDtoList(Funding funding) {
        List<FundingItem> fundingItemList = funding.getFundingItems();
        List<MyPageFundingItemDto> myPageFundingItemList = new ArrayList<>();
        int collectPrice = funding.getCollectPrice();

        for (FundingItem fundingItem : fundingItemList) {
            int itemPercent;
            if (collectPrice >= fundingItem.getItem().getItemPrice()) {
                collectPrice -= fundingItem.getItem().getItemPrice();
                itemPercent = 100;
            } else if (collectPrice > 0) {
                itemPercent = collectPrice * 100 / fundingItem.getItem().getItemPrice();
                collectPrice = 0;
            } else {
                itemPercent = 0;
            }

            myPageFundingItemList.add(
                    MyPageFundingItemDto.fromEntity(funding, fundingItem, itemPercent));
        }

        return myPageFundingItemList;
    }

    public MyFundingHistoryDto getMyFundingHistory(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);

        List<Funding> fundings = fundingRepository.findFundingByMemberId(memberId);

        List<MyPageFundingDetailHistoryDto> myPageFundingDetailHistoryDtos = fundings.stream()
                .map(funding -> {
                    int contributors = contributorRepository.countContributorsForFunding(funding.getFundingId());
                    int fundingPercent = FundingUtils.calculateFundingPercent(funding);
                    return MyPageFundingDetailHistoryDto.fromEntity(funding, contributors, fundingPercent);
                })
                .toList();

        return MyFundingHistoryDto.fromEntity(myPageMemberDto, myPageFundingDetailHistoryDtos);
    }

    public MyFundingHistoryDetailDto getMyFundingHistoryDetails(Long fundingId) {
        Funding funding = fundingRepository.findMemberById(fundingId);
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(funding.getMember());
        if (funding.isFundingStatus()) {
            // 펀딩이 진행중인 상황
            throw new CommonException(INVALID_FUNDING_STATUS);
        }
        List<MyPageFundingItemDto> myPageFundingItemDtoList = getMyPageFundingItemDtoList(funding);
        List<ParticipateFriendDto> participateFriendDtoList = getParticipateFriendDtoList(funding);
        int totalPercent = FundingUtils.calculateFundingPercent(funding);
        return MyFundingHistoryDetailDto.createMyFundingHistoryDetailDto(
                myPageMemberDto,
                myPageFundingItemDtoList,
                participateFriendDtoList,
                totalPercent,
                funding.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                funding.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    public FriendFundingHistoryDto getFriendFundingHistory(Long memberId) {

        List<FriendFundingContributionDto> friendFundingContributionDtoList =
                contributorRepository.findAllByMemberId(memberId).stream()
                        .map(contributor -> {
                            Funding contributeFunding = contributor.getFunding();
                            return FriendFundingContributionDto.fromEntity(contributor, contributeFunding);
                        })
                        .toList();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);

        return FriendFundingHistoryDto.fromEntity(myPageMemberDto, friendFundingContributionDtoList);
    }

    public FundingRegisterStatusDto getRegisterFunding(Long memberId) {
        Optional<Funding> funding = fundingRepository.findByMemberIdAndStatus(memberId, true);

        return FundingRegisterStatusDto.builder()
                .isRegisterFunding(funding.isPresent())
                .build();
    }
}
