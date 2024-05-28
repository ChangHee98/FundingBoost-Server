package kcs.funding.fundingboost.domain.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.common.BaseTimeEntity;
import kcs.funding.fundingboost.domain.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "relationship")
public class Relationship extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "relation_id")
    private Long relationId;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @JoinColumn(name = "friend_id")
    @ManyToOne(fetch = LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member friend;

    /**
     * 나의 관계에 친구를 추가하고, 친구의 관계에 나를 추가하여 List로 반환
     */
    public static List<Relationship> createRelationships(Member member, Member friend) {

        List<Relationship> relationships = new ArrayList<>();
        Relationship myRelationship = new Relationship();
        myRelationship.member = member;
        myRelationship.friend = friend;

        Relationship friendRelationship = new Relationship();
        friendRelationship.member = friend;
        friendRelationship.friend = member;

        relationships.add(myRelationship);
        relationships.add(friendRelationship);
        return relationships;
    }
}
