package kcs.funding.fundingboost.domain.repository.fundingItem;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FundingItemRepository extends JpaRepository<FundingItem, Long> {

    @Query("select fi from FundingItem fi" +
            " join fetch fi.funding f" +
            " join fetch fi.item i" +
            " where f.fundingId = :fundingId")
    List<FundingItem> findAllByFundingId(@Param("fundingId") Long fundingId);

    @Query("select fi from FundingItem fi" +
            " join fetch fi.funding f" +
            " where f.fundingId = :fundingId")
    List<FundingItem> findFundingItemIdListByFunding(@Param("fundingId") Long fundingId);


    @Query("select fi from FundingItem fi" +
            " join fetch fi.item i" +
            " where fi.fundingItemId = :fundingItemId")
    FundingItem findFundingItemAndItemByFundingItemId(@Param("fundingItemId") Long fundingItemId);

    @Query("select fi from FundingItem fi" +
            " join fetch fi.funding f" +
            " join fetch f.member" +
            " where fi.fundingItemId = :fundingItemId")
    Optional<FundingItem> findFundingItemByFundingItemId(@Param("fundingItemId") Long fundingItemId);
}