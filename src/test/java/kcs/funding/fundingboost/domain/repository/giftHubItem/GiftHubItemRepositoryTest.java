package kcs.funding.fundingboost.domain.repository.giftHubItem;


import static org.assertj.core.api.Assertions.assertThat;

import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(QueryDslConfig.class)
class GiftHubItemRepositoryTest {

    @Autowired
    private GiftHubItemRepository giftHubItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;
    private Item item;
    private GiftHubItem giftHubItem;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");
        item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                "샤넬", "뷰티", "00:00");
        giftHubItem = GiftHubItem.createGiftHubItem(1, item, member);
        testEntityManager.persist(member);
        testEntityManager.persist(item);
        testEntityManager.persist(giftHubItem);
        testEntityManager.clear();
    }

    @Test
    @DisplayName("GiftHubItemId와 MemberId로 GiftHubItem 찾기 - 성공")
    void findGiftHubItemByGiftHubItemIdAndMemberId() {
        //when
        GiftHubItem result = giftHubItemRepository.findGiftHubItemByGiftHubItemIdAndMemberId(
                giftHubItem.getGiftHubItemId(),
                member.getMemberId()).get();
        //then
        assertThat(result.getGiftHubItemId()).isEqualTo(giftHubItem.getGiftHubItemId());
        assertThat(result.getMember().getNickName()).isEqualTo(member.getNickName());
        assertThat(result.getItem().getItemName()).isEqualTo(item.getItemName());
    }
}