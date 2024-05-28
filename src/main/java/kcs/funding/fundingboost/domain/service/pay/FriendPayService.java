package kcs.funding.fundingboost.domain.service.pay;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_FUNDING_MONEY;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_FUNDING;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import kcs.funding.fundingboost.domain.aop.lock.RedisLock;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.pay.friendFundingPay.FriendPayProcessDto;
import kcs.funding.fundingboost.domain.dto.response.pay.friendFundingPay.FriendFundingPayingDto;
import kcs.funding.fundingboost.domain.entity.Contributor;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.contributor.ContributorRepository;
import kcs.funding.fundingboost.domain.repository.funding.FundingRepository;
import kcs.funding.fundingboost.domain.service.utils.PayUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FriendPayService {

    private final MemberRepository memberRepository;
    private final FundingRepository fundingRepository;
    private final ContributorRepository contributorRepository;

    public FriendFundingPayingDto getFriendFundingPay(Long fundingId, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
        Funding friendFunding = fundingRepository.findById(fundingId).orElseThrow();

        return FriendFundingPayingDto.fromEntity(friendFunding, member.getPoint());
    }

    @RedisLock(key = "lock")
    @Transactional
    public CommonSuccessDto fund(Long memberId, Long fundingId,
                                 FriendPayProcessDto friendPayProcessDto) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        int point = friendPayProcessDto.usingPoint();
        int contributePrice = friendPayProcessDto.fundingPrice();
        PayUtils.deductPointsIfPossible(findMember, point); // 내 포인트 차감

        Funding friendFunding = fundingRepository.findById(fundingId)
                .orElseThrow(() -> new CommonException(NOT_FOUND_FUNDING));

        if (friendFunding.getCollectPrice() + contributePrice <= friendFunding.getTotalPrice()) {
            Contributor contributor = Contributor.createContributor(contributePrice, findMember, friendFunding);
            contributorRepository.save(contributor);
        } else {
            throw new CommonException(INVALID_FUNDING_MONEY);
        }
        return CommonSuccessDto.fromEntity(true);
    }
}
