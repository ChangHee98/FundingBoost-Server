package kcs.funding.fundingboost.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Delivery;
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
class DeliveryRepositoryTest {
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;
    private Delivery delivery;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

        delivery = Delivery.createDelivery("경기도 성남시 분당구 판교역로 166", "010-1234-5678", "장이수", member);

        testEntityManager.persist(member);
        testEntityManager.persist(delivery);
    }

    @DisplayName("findAllByMemberId 테스트")
    @Test
    void findAllByMemberId() {
        //when
        List<Delivery> result = deliveryRepository.findAllByMemberId(member.getMemberId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAddress()).isEqualTo(delivery.getAddress());
    }
}