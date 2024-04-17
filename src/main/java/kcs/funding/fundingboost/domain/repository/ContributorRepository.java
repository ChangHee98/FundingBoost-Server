package kcs.funding.fundingboost.domain.repository;

import kcs.funding.fundingboost.domain.entity.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContributorRepository extends JpaRepository<Contributor, Long> {

    @Query("select c from Contributor c" +
            " join fetch c.funding f" +
            " where f.fundingId = :fundingId")
    List<Contributor> findByFundingId(@Param("fundingId") Long fundingId);
}
