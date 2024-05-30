package kcs.funding.fundingboost.domain.repository.fundingItem;

import static kcs.funding.fundingboost.domain.entity.Tag.BIRTHDAY;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@Slf4j
@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FundingItemRepositoryTest {

    @Autowired
    private FundingItemRepository fundingItemRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;
    private Item item;
    private Funding funding;
    private FundingItem fundingItem;

    @BeforeEach
    void setUp() {
        item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                "샤넬", "뷰티", "00:00");

        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

        funding = Funding.createFunding(member, "생일축하해주세욥 3월21일입니닷", BIRTHDAY,
                LocalDateTime.now().plusDays(15));

        fundingItem = FundingItem.createFundingItem(funding, item, 1);

        testEntityManager.persist(item);
        testEntityManager.persist(member);
        testEntityManager.persist(funding);
        testEntityManager.persist(fundingItem);
    }


    @DisplayName("findAllByFundingId 테스트")
    @Test
    void findAllByFundingId() {
        //when
        List<FundingItem> result = fundingItemRepository.findAllByFundingId(funding.getFundingId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItem().getItemName()).isEqualTo(item.getItemName());
    }

    @DisplayName("findFundingItemIdListByFundingId 테스트")
    @Test
    void findFundingItemIdListByFundingId() {
        //when
        List<FundingItem> result = fundingItemRepository.findFundingItemIdListByFundingId(funding.getFundingId());

        //then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getItem().getItemName()).isEqualTo(item.getItemName());
    }

    @DisplayName("findFundingItemAndItemByFundingItemId 테스트")
    @Test
    void findFundingItemAndItemByFundingItemId() {
        //when
        FundingItem result = fundingItemRepository.findFundingItemAndItemByFundingItemId(
                fundingItem.getFundingItemId());

        //then
        assertThat(result.getItem().getItemName()).isEqualTo(item.getItemName());
    }

    @DisplayName("findFundingItemByFundingItemId 테스트")
    @Test
    void findFundingItemByFundingItemId() {
        //when
        Optional<FundingItem> result = fundingItemRepository.findFundingItemByFundingItemId(
                fundingItem.getFundingItemId());

        //then
        assertThat(result.get().getItem().getItemName()).isEqualTo(item.getItemName());
    }
}