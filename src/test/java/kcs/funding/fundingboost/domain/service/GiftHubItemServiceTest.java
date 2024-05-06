package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_GIFTHUB_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_ITEM;
import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.common.CommonSuccessDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.AddGiftHubDto;
import kcs.funding.fundingboost.domain.dto.request.giftHub.ItemQuantityDto;
import kcs.funding.fundingboost.domain.dto.response.giftHub.GiftHubDto;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.ItemRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.giftHubItem.GiftHubItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GiftHubItemServiceTest {

    @Mock
    private GiftHubItemRepository giftHubItemRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private GiftHubItemService giftHubItemService;

    private Member member;
    private Item item1;
    private Item item2;
    private GiftHubItem giftHubItem1;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        item1 = ItemFixture.item1();
        item2 = ItemFixture.item2();
        giftHubItem1 = GiftHubItem.createGiftHubItem(1, item1, member);
    }

    @DisplayName("로그인 한 사용자의 기프트 허브 정보")
    @Test
    void getGiftHub_ReturnsGiftHubDtoList_WhenMemberExists() {
        //given
        GiftHubItem giftHubItem1 = GiftHubItem.createGiftHubItem(1, item1, member);
        GiftHubItem giftHubItem2 = GiftHubItem.createGiftHubItem(2, item2, member);

        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));
        when(giftHubItemRepository.findGiftHubItemsByMember(member.getMemberId())).thenReturn(
                List.of(giftHubItem1, giftHubItem2));

        // when
        List<GiftHubDto> result = giftHubItemService.getGiftHub(member.getMemberId());

        //then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(2, result.size());

        verify(memberRepository).findById(member.getMemberId());
        verify(giftHubItemRepository).findGiftHubItemsByMember(member.getMemberId());
    }

    @DisplayName("로그인 안한 사용자의 기프트 허브 정보")
    @Test
    public void getGiftHub_ThrowsException_WhenMemberDoesNotExist() {
        // given
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.empty());

        // when & then
        assertThrows(CommonException.class, () -> giftHubItemService.getGiftHub(member.getMemberId()));

        verify(memberRepository).findById(member.getMemberId());
        verifyNoInteractions(giftHubItemRepository);
    }

    @DisplayName("GiftHubItem 추가 성공")
    @Test
    void addGiftHub() {
        //given
        AddGiftHubDto addGiftHubDto = new AddGiftHubDto(member.getMemberId(), 1);

        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));
        when(itemRepository.findById(item2.getItemId())).thenReturn(Optional.of(item2));
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));

        // when
        CommonSuccessDto result1 = giftHubItemService.addGiftHub(item1.getItemId(), addGiftHubDto);
        CommonSuccessDto result2 = giftHubItemService.addGiftHub(item2.getItemId(), addGiftHubDto);

        // then
        assertNotNull(result1);
        assertTrue(result1.isSuccess());
        assertNotNull(result2);
        assertTrue(result2.isSuccess());
        verify(itemRepository).findById(item1.getItemId());
        verify(itemRepository).findById(item2.getItemId());
        verify(giftHubItemRepository, times(2)).save(any(GiftHubItem.class));
    }

    @DisplayName("GiftHubItem 추가 실패 - 아이템을 찾을 수 없음")
    @Test
    void addGiftHub_Fail_ItemNotFound() {
        // given
        AddGiftHubDto addGiftHubDto = new AddGiftHubDto(member.getMemberId(), 1);

        // when
        Exception exception = assertThrows(CommonException.class, () -> {
            giftHubItemService.addGiftHub(1L, addGiftHubDto);
        });

        // then
        assertEquals(NOT_FOUND_ITEM.getMessage(), exception.getMessage());
        verify(giftHubItemRepository, times(0)).save(any(GiftHubItem.class));
    }

    @DisplayName("GiftHubItem 추가 실패 - 멤버를 찾을 수 없음")
    @Test
    void addGiftHub_Fail_MemberNotFound() {
        // given
        when(itemRepository.findById(item1.getItemId())).thenReturn(Optional.of(item1));
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.empty());
        AddGiftHubDto addGiftHubDto = new AddGiftHubDto(member.getMemberId(), 1);

        // when
        Exception exception = assertThrows(CommonException.class, () -> {
            giftHubItemService.addGiftHub(item1.getItemId(), addGiftHubDto);
        });

        // then
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
        verify(giftHubItemRepository, times(0)).save(any(GiftHubItem.class));
    }

    @DisplayName("GiftHubItem 수량 변경 성공")
    @Test
    void updateItem_Success() {
        //given
        ItemQuantityDto itemQuantityDto = new ItemQuantityDto(10);
        GiftHubItem mockGiftHubItem = mock(GiftHubItem.class);

        when(giftHubItemRepository.findById(giftHubItem1.getGiftHubItemId())).thenReturn(Optional.of(mockGiftHubItem));

        //when
        CommonSuccessDto result = giftHubItemService.updateItem(giftHubItem1.getGiftHubItemId(), itemQuantityDto);

        //then
        verify(giftHubItemRepository).findById(giftHubItem1.getGiftHubItemId());
        verify(mockGiftHubItem).updateQuantity(itemQuantityDto.quantity());
        assertTrue(result.isSuccess());
    }

    @DisplayName("GiftHubItem 수량 변경 실패 - 아이템 조회 실패")
    @Test
    void updateItem_ItemNotFound() {
        // 준비
        Long gifthubItemId = giftHubItem1.getGiftHubItemId();
        ItemQuantityDto itemQuantityDto = new ItemQuantityDto(10);

        when(giftHubItemRepository.findById(gifthubItemId)).thenReturn(Optional.empty());

        // 실행 & 검증
        Exception exception = assertThrows(CommonException.class, () -> {
            giftHubItemService.updateItem(gifthubItemId, itemQuantityDto);
        });

        assertEquals(NOT_FOUND_GIFTHUB_ITEM.getMessage(), exception.getMessage());
    }

    @DisplayName("GiftHubItem 삭제 성공")
    @Test
    void deleteGiftHubItem_Success() {
        //given
        when(giftHubItemRepository.findGiftHubItemByGiftHubItemIdAndMemberId(giftHubItem1.getGiftHubItemId(),
                member.getMemberId())).thenReturn(Optional.of(giftHubItem1));

        //when
        CommonSuccessDto result = giftHubItemService.deleteGiftHubItem(member.getMemberId(),
                giftHubItem1.getGiftHubItemId());

        //then
        assertTrue(result.isSuccess());
        verify(giftHubItemRepository).deleteById(giftHubItem1.getGiftHubItemId());
    }

    @DisplayName("GiftHubItem 삭제 실패 - 아이템 조회 실패")
    @Test
    void deleteGiftHubItem_ItemNotFound() {
        //given
        when(giftHubItemRepository.findGiftHubItemByGiftHubItemIdAndMemberId(giftHubItem1.getGiftHubItemId(),
                member.getMemberId())).thenReturn(Optional.empty());

        //when
        CommonException exception = assertThrows(CommonException.class,
                () -> giftHubItemService.deleteGiftHubItem(member.getMemberId(), giftHubItem1.getGiftHubItemId()));

        //then
        assertEquals(NOT_FOUND_GIFTHUB_ITEM.getMessage(), exception.getMessage());
        verify(giftHubItemRepository, never()).deleteById(anyLong());
    }
}
