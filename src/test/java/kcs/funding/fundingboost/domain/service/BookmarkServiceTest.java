package kcs.funding.fundingboost.domain.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.ItemRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.bookmark.BookmarkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

    @Mock
    private BookmarkRepository bookmarkRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private BookmarkService bookmarkService;

    private Member member;

    private Item item;

    @BeforeEach
    void beforeEach() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        item = ItemFixture.item1();
    }

    @Test
    void getMyWishList() {
    }

    @DisplayName("북마크가 존재할 때 : 북마크 삭제")
    @Test
    void givenBookmarkExists_whenToggleItemLike_thenDeleteBookmark() {
        //given
        Bookmark bookmark = Bookmark.createBookmark(member, item);

        when(bookmarkRepository.findBookmarkByMemberAndItem(member.getMemberId(), item.getItemId()))
                .thenReturn(Optional.of(bookmark));

        //when
        CommonSuccessDto result = bookmarkService.toggleItemLike(member.getMemberId(), item.getItemId());

        // then
        verify(bookmarkRepository, times(1)).delete(bookmark);
        assertTrue(result.isSuccess());
    }

    @DisplayName("북마크가 존재하지 않을 때 : 북마크 저장")
    @Test
    void givenBookmarkNotExists_whenToggleItemLike_thenCreateBookmark() {
        //given
        when(bookmarkRepository.findBookmarkByMemberAndItem(member.getMemberId(), item.getItemId())).thenReturn(
                Optional.empty());
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(itemRepository.findById(item.getItemId())).thenReturn(Optional.of(item));

        //when
        CommonSuccessDto result = bookmarkService.toggleItemLike(member.getMemberId(), item.getItemId());

        // then
        //bookmarkRepository의 save 메소드가 한 번 호출되었는지 확인하고, 그 호출에 사용된 파라미터의 타입이 Bookmark 클래스의 인스턴스임을 확인하지만, 구체적인 인스턴스 값까지는 검사하지 않는다.
        verify(bookmarkRepository, times(1)).save(any(Bookmark.class));
        assertTrue(result.isSuccess());
    }
}