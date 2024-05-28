package kcs.funding.fundingboost.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.response.shopping.ShopDto;
import kcs.funding.fundingboost.domain.dto.response.shoppingDetail.ItemDetailDto;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.bookmark.BookmarkRepository;
import kcs.funding.fundingboost.domain.repository.item.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookmarkRepository bookmarkRepository;
    @InjectMocks
    private ItemService itemService;
    private Member member;
    private Item item;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        item = ItemFixture.item1();
    }

    @DisplayName("아이템 조회")
    @Test
    void getItems() throws NoSuchFieldException, IllegalAccessException {
        //given
        List<Item> items = ItemFixture.items3();
        Pageable pageable = Pageable.ofSize(3);
        Slice<Item> itemSlice = new SliceImpl<>(items, pageable, false);
        when(itemRepository.findItemsByCategory(4L, "뷰티", pageable)).thenReturn(itemSlice);
        //when
        Slice<ShopDto> result = itemService.getItems(4L, items.get(0).getCategory(), pageable);

        //then
        assertNotNull(result);
        assertEquals(items.size(), result.getSize());
        verify(itemRepository, times(1)).findItemsByCategory(4L, "뷰티", pageable);
    }

    @DisplayName("아이템 상세 조회 : 북마크가 존재하는 경우")
    @Test
    void getItemDetail_WhenBookmarkExists() {
        //given
        Bookmark bookmark = Bookmark.createBookmark(member, item);

        when(bookmarkRepository.findBookmarkByMemberAndItem(member.getMemberId(), item.getItemId())).thenReturn(
                Optional.of(bookmark));

        //when
        ItemDetailDto result = itemService.getItemDetail(member.getMemberId(), item.getItemId());

        //then
        assertNotNull(result);
        Assertions.assertTrue(result.bookmark());
        verify(bookmarkRepository, times(1)).findBookmarkByMemberAndItem(member.getMemberId(), item.getItemId());
    }

    @DisplayName("아이템 상세 조회 : 북마크가 존재하지 않는 경우")
    @Test
    void getItemDetail_WhenBookmarkDoesNotExist() {
        //given
        when(bookmarkRepository.findBookmarkByMemberAndItem(member.getMemberId(), item.getItemId()))
                .thenReturn(Optional.empty());
        when(itemRepository.findById(item.getItemId()))
                .thenReturn(Optional.of(item));

        //when
        ItemDetailDto result = itemService.getItemDetail(member.getMemberId(), item.getItemId());

        //then
        assertNotNull(result);
        Assertions.assertFalse(result.bookmark());
        verify(itemRepository, times(1)).findById(item.getItemId());
    }
}