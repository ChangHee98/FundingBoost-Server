package kcs.funding.fundingboost.domain.service;

import java.util.Comparator;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.myPage.myFundingStatus.TransformPointDto;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.exception.ErrorCode;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final FundingRepository fundingRepository;

    @Transactional
    public CommonSuccessDto exchangePoint(TransformPointDto transformPointDto) {
        Funding funding = fundingRepository.findMemberByFundingId(transformPointDto.fundingId());
        if (funding == null) {
            throw new CommonException(ErrorCode.NOT_FOUND_FUNDING);
        }
        Member member = funding.getMember();
        List<FundingItem> fundingItems = funding.getFundingItems();

        List<FundingItem> sortedFundingItems = fundingItems.stream()
                .sorted(Comparator.comparingInt(FundingItem::getItemSequence))
                .toList();

        int collectPrice = funding.getCollectPrice();
        for (FundingItem sortedFundingItem : sortedFundingItems) {
            if (!sortedFundingItem.isItemStatus()) {
                // 펀딩 완료된 상태라면
                collectPrice -= sortedFundingItem.getItem().getItemPrice();
            } else {
                member.plusPoint(collectPrice);
                sortedFundingItem.finishFundingItem(); // fundingItem을 finish 상태로 변경
                return CommonSuccessDto.fromEntity(true);
            }
        }
        return CommonSuccessDto.fromEntity(true);
    }
}
