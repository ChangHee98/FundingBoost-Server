package kcs.funding.fundingboost.domain.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QueryDslConfig.class)
class ItemRepositoryTest {
    // @Autowired
    // private ItemRepository itemRepository;

    // @Autowired
    // private TestEntityManager testEntityManager;

    // private List<Item> items;
    // private List<Long> itemIds;

    // @BeforeEach
    // void setUp() {
    //     Item item1 = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
    //             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
    //             "샤넬", "뷰티", "00:00");
    //     Item item2 = Item.createItem("NEW 루쥬 코코 밤(+샤넬 기프트 카드)", 51000,
    //             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg",
    //             "샤넬", "뷰티", "934 코랄린 [NEW]");

    //     Item item3 = Item.createItem("코코 마드모아젤 헤어 미스트 35ml", 85000,
    //             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg",
    //             "샤넬", "뷰티", "코코 마드모아젤 헤어 미스트 35ml");

    //     testEntityManager.persist(item1);
    //     testEntityManager.persist(item2);
    //     testEntityManager.persist(item3);

    //     items = List.of(item1, item2, item3);

    //     itemIds = items.stream()
    //             .map(Item::getItemId)
    //             .collect(Collectors.toList());
    // }

    // @DisplayName("findItemsByItemIds 테스트")
    // @Test
    // void findItemsByItemIds() {
    //     //when
    //     List<Item> result = itemRepository.findItemsByItemIds(itemIds);

    //     //then
    //     assertThat(result).hasSize(3);
    //     assertThat(result.get(0).getItemName()).isEqualTo(items.get(0).getItemName());
    // }
}