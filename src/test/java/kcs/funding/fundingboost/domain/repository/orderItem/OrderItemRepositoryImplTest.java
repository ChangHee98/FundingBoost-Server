package kcs.funding.fundingboost.domain.repository.orderItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
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
@AutoConfigureTestDatabase(replace = NONE)
class OrderItemRepositoryImplTest {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;
    
    private Member member;
    private Delivery delivery;
    private Item item;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

        delivery = Delivery.createDelivery("경기도 성남시 분당구 판교역로 166", "010-1234-5678", "장이수", member);

        item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                "샤넬", "뷰티", "00:00");

        order = Order.createOrder(member, delivery);

        orderItem = orderItem.createOrderItem(order, item, 1);

        testEntityManager.persist(member);
        testEntityManager.persist(delivery);
        testEntityManager.persist(item);
        testEntityManager.persist(order);
        testEntityManager.persist(orderItem);
    }

    @DisplayName("findLastOrderByMemberId 테스트")
    @Test
    void findLastOrderByMemberId() {
        //when
        List<OrderItem> result = orderItemRepository.findLastOrderByMemberId(member.getMemberId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItem().getItemName()).isEqualTo(item.getItemName());
    }
}