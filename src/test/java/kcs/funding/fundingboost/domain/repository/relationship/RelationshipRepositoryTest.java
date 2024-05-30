package kcs.funding.fundingboost.domain.repository.relationship;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Relationship;
import kcs.funding.fundingboost.domain.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class RelationshipRepositoryTest {

    @Autowired
    private RelationshipRepository relationshipRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    
    private Member member;
    private Member member2;
    private Member member3;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

        member2 = Member.createMemberWithPoint("구태형", "rnxogud136@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg",
                999999999, "aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA");

        member3 = Member.createMemberWithPoint("맹인호", "aoddlsgh98@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg",
                200000, "aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ");

        List<Member> friends = List.of(member2, member3);

        List<Relationship> relationships = new ArrayList<>();

        for (Member friend : friends) {
            relationships.add(Relationship.createRelationships(member, friend).get(0));
        }
        testEntityManager.persist(member);
        testEntityManager.persist(member2);
        testEntityManager.persist(member3);
        for (Relationship relationship : relationships) {
            testEntityManager.persist(relationship);
        }
    }

    @DisplayName("findItemsByItemIds 테스트")
    @Test
    void findFriendByMemberId() {

        //when
        List<Relationship> result = relationshipRepository.findFriendByMemberId(member.getMemberId());

        //then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getMember().getNickName()).isEqualTo(member.getNickName());
    }
}