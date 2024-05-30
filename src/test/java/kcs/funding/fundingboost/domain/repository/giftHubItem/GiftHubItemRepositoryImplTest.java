package kcs.funding.fundingboost.domain.repository.giftHubItem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import java.util.List;
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
@AutoConfigureTestDatabase(replace = NONE)
class GiftHubItemRepositoryImplTest {

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
    @DisplayName("사용자로 GiftHub 아이템 목록 조회 - 성공")
    void findGiftHubItemsByMember() {
        //given
        List<GiftHubItem> result = giftHubItemRepository.findGiftHubItemsByMember(member.getMemberId());

        //then
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getGiftHubItemId()).isEqualTo(giftHubItem.getGiftHubItemId());
        assertThat(result.get(0).getMember().getNickName()).isEqualTo(member.getNickName());
        assertThat(result.get(0).getItem().getItemName()).isEqualTo(item.getItemName());
    }
}