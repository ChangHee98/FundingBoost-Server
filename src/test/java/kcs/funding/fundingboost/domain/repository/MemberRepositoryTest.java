package kcs.funding.fundingboost.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");
        testEntityManager.persist(member);
        testEntityManager.clear();
    }

    @Test
    @DisplayName("닉네임으로 사용자 검색 - 성공")
    void findByNickName() {
        //when
        Optional<Member> searchMember = memberRepository.findByNickName(member.getNickName());

        //then
        assertThat(searchMember).isPresent();
        assertThat(searchMember.get().getNickName()).isEqualTo(member.getNickName());
        assertThat(searchMember.get().getMemberId()).isEqualTo(member.getMemberId());
    }
}