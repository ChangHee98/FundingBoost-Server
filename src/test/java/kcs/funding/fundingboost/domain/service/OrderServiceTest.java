package kcs.funding.fundingboost.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory.OrderHistoryDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.ItemFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.OrderFixture;
import kcs.funding.fundingboost.domain.model.OrderItemFixture;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.orderItem.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @InjectMocks
    private OrderService orderService;
    private Member member;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
    }

    @DisplayName("주문 목록이 존재하는 경우")
    @Test
    void getOrderHistory_WhenOrderListExists() throws NoSuchFieldException, IllegalAccessException {
        //given
        Item item = ItemFixture.item1();
        Delivery delivery = DeliveryFixture.address1(member);
        Order order = OrderFixture.order1(member, delivery);
        OrderItem orderItem = OrderItemFixture.quantity1(order, item);

        when(orderItemRepository.findLastOrderByMemberId(member.getMemberId())).thenReturn(
                List.of(orderItem));

        //when
        OrderHistoryDto result = orderService.getOrderHistory(member.getMemberId());

        //then
        assertNotNull(result);
        assertThat(result.orderHistoryItemDtoList()).hasSize(1).extracting("itemName").contains(item.getItemName());
        verify(orderItemRepository, times(1)).findLastOrderByMemberId(member.getMemberId());
    }


    @DisplayName("주문 목록이 존재하지 경우")
    @Test
    void getOrderHistory_WhenOrderListDoesNotExist() {
        //given
        when(orderItemRepository.findLastOrderByMemberId(member.getMemberId())).thenReturn(Collections.emptyList());
        when(memberRepository.findById(member.getMemberId())).thenReturn(Optional.of(member));

        //when
        OrderHistoryDto result = orderService.getOrderHistory(member.getMemberId());

        //then
        assertNotNull(result);
        assertThat(result.orderHistoryItemDtoList()).isNull();
        verify(memberRepository, times(1)).findById(member.getMemberId());
    }
}