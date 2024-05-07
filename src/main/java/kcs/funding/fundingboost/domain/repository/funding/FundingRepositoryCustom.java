package kcs.funding.fundingboost.domain.repository.funding;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.Funding;

public interface FundingRepositoryCustom {

    Optional<Funding> findFundingInfo(Long memberId);

    Funding findMemberByFundingId(Long fundingId);

    List<Funding> findFundingByMemberId(Long memberId);
}
