package kcs.funding.fundingboost.domain.repository.relationship;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RelationshipRepository extends JpaRepository<Relationship, Long>,
        RelationshipRepositoryCustom {

    @Query("select r from Relationship r"
            + " join fetch r.member m"
            + " where m.memberId=:memberId")
    List<Relationship> findFriendByMemberId(@Param("memberId") Long memberId);
}