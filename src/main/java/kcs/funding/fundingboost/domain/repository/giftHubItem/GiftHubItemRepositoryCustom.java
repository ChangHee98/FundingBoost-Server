package kcs.funding.fundingboost.domain.repository.giftHubItem;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;

public interface GiftHubItemRepositoryCustom {
    List<GiftHubItem> findGiftHubItemsByMember(Long memberId);
}
