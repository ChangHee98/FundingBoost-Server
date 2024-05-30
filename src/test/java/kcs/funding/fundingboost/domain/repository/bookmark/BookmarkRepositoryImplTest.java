package kcs.funding.fundingboost.domain.repository.bookmark;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Bookmark;
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

@DataJpaTest
@Import(QueryDslConfig.class)
@Slf4j
class BookmarkRepositoryImplTest {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Member member;
    private Item item;
    private Bookmark bookmark;

    @BeforeEach
    void setUp() {
        member = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");
        item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                "샤넬", "뷰티", "00:00");
        bookmark = Bookmark.createBookmark(member, item);
        testEntityManager.persist(member);
        testEntityManager.persist(item);
        testEntityManager.persist(bookmark);
        testEntityManager.clear();
    }

    @Test
    @DisplayName("멤버와 아이템으로 bookmark 찾기 - 성공")
    void findBookmarkByMemberAndItem() {
        //when
        Optional<Bookmark> result = bookmarkRepository.findBookmarkByMemberAndItem(member.getMemberId(),
                bookmark.getItem().getItemId());

        //then
        assertThat(result.get().getMember().getNickName()).isEqualTo(bookmark.getMember().getNickName());
        assertThat(result.get().getItem().getItemName()).isEqualTo(item.getItemName());
    }
}