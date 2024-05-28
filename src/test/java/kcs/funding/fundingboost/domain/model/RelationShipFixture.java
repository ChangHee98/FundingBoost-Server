package kcs.funding.fundingboost.domain.model;

import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Relationship;
import kcs.funding.fundingboost.domain.entity.member.Member;

public class RelationShipFixture {

    /**
     * 사용자의 관계목록만 반환
     */
    public static List<Relationship> myRelationships(Member member, List<Member> friendList) {
        List<Relationship> relationships = new ArrayList<Relationship>();

        for (Member friend : friendList) {
            relationships.add(Relationship.createRelationships(member, friend).get(0));
        }

        return relationships;
    }
}
