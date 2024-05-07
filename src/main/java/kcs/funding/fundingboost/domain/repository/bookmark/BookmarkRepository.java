package kcs.funding.fundingboost.domain.repository.bookmark;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long>, BookmarkRepositoryCustom {

    @Query("select b from Bookmark b" +
            " join fetch b.member m" +
            " join fetch b.item i" +
            " where m.memberId = :memberId")
    List<Bookmark> findAllByMemberId(@Param("memberId") Long memberId);
}
