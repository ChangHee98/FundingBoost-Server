package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryManageDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.repository.DeliveryRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceTest {
    @Mock
    private DeliveryRepository deliveryRepository;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private DeliveryService deliveryService;

    private Member member;
    private Delivery delivery;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        delivery = DeliveryFixture.address1(member);
    }

    @DisplayName("마이페이지 - getMyDeliveryManageList : 배송지 조회를 성공 deliveryList를 가져와서 Dto에 넣어주어야 한다")
    @Test
    void getMyDeliveryManageList() throws NoSuchFieldException, IllegalAccessException {
        //given
        List<Delivery> deliveryList = DeliveryFixture.addresses3(member);

        when(deliveryRepository.findAllByMemberId(anyLong())).thenReturn(deliveryList);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        //when
        MyPageDeliveryManageDto myPageDeliveryManageDto = deliveryService.getMyDeliveryManageList(member.getMemberId());
        List<MyPageDeliveryDto> myPageDeliveryDtos = myPageDeliveryManageDto.myPageDeliveryDtoList();

        //then
        for (int i = 0; i < deliveryList.size(); i++) {
            assertEquals(deliveryList.get(i).getCustomerName(), myPageDeliveryDtos.get(i).customerName());
            assertEquals(deliveryList.get(i).getAddress(), myPageDeliveryDtos.get(i).address());
            assertEquals(deliveryList.get(i).getPhoneNumber(), myPageDeliveryDtos.get(i).phoneNumber());
        }
    }

    @DisplayName("마이페이지 - 배송지 조회 성공 - getMyDeliveryManageList : 배송지 목록이 없으면 빈 리스트를 반환해야 한다")
    @Test
    void getMyDeliveryManageList_DeliveryNotFound() {
        //given
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        //when
        MyPageDeliveryManageDto myPageDeliveryManageDto = deliveryService.getMyDeliveryManageList(member.getMemberId());
        //then
        assertEquals(List.of(), myPageDeliveryManageDto.myPageDeliveryDtoList());
    }

    @DisplayName("마이페이지 - 배송지 조회 실패 - getMyDeliveryManageList : 사용자가 없으면 NOT_FOUND_MEMBER 예외가 발생해야 한다")
    @Test
    void getMyDeliveryManageList_MemberNotFound() {
        //given
        List<Delivery> deliveryList = Collections.singletonList(delivery);

        when(deliveryRepository.findAllByMemberId(anyLong())).thenReturn(deliveryList);
        when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when & then
        CommonException exception = assertThrows(CommonException.class, () -> {
            deliveryService.getMyDeliveryManageList(anyLong());
        }); // 사용자를 찾지 못했으므로 실패해야함
        assertEquals(NOT_FOUND_MEMBER.getMessage(), exception.getMessage());
    }
}