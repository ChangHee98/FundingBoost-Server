package kcs.funding.fundingboost.domain.repository.bookmark;

import java.util.Optional;
import kcs.funding.fundingboost.domain.entity.Bookmark;

public interface BookmarkRepositoryCustom {

    Optional<Bookmark> findBookmarkByMemberAndItem(Long memberId, Long itemId);
}
