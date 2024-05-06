package kcs.funding.fundingboost.domain.repository.giftHubItem;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GiftHubItemRepository extends JpaRepository<GiftHubItem, Long>, GiftHubItemRepositoryCustom {

    @Query("select g from GiftHubItem g" +
            " join fetch g.item i" +
            " join fetch g.member m" +
            " where i.itemId in :itemIds" +
            " and m.memberId = :memberId")
    List<GiftHubItem> findGiftHubItemByMemberIdAndItemIds(@Param("memberId") Long memberId,
                                                          @Param("itemIds") List<Long> itemIds);

    @Query("select g from GiftHubItem g" +
            " join fetch g.member m" +
            " where g.giftHubItemId = :giftHubItemId" +
            " and m.memberId = :memberId ")
    Optional<GiftHubItem> findGiftHubItemByGiftHubItemIdAndMemberId(@Param("giftHubItemId") Long giftHubItemId,
                                                                    @Param("memberId") Long memberId);
}
