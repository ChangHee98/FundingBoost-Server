package kcs.funding.fundingboost.domain.repository.funding;

import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.Funding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FundingRepository extends JpaRepository<Funding, Long>, FundingRepositoryCustom {
    @Query("select f from Funding f" +
            " join fetch f.member m" +
            " where m.memberId = :memberId and " +
            " f.fundingStatus = :status")
    Optional<Funding> findByMemberIdAndStatus(@Param("memberId") Long memberId, @Param("status") boolean status);

    @Query("select f from Funding f" +
            " join fetch f.member m" +
            " where f.fundingId = :fundingId")
    Funding findMemberById(@Param("fundingId") Long fundingId);
}
