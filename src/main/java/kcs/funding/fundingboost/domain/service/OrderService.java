package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory.OrderHistoryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.orderHistory.OrderHistoryItemDto;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import kcs.funding.fundingboost.domain.repository.orderItem.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderHistoryDto getOrderHistory(Long memberId) {
        List<OrderItem> orderItemList = orderItemRepository.findLastOrderByMemberId(memberId);

        if (!orderItemList.isEmpty()) {
            // 주문 목록이 존재하는 경우
            List<OrderHistoryItemDto> orderHistoryItemDtoList = orderItemList.stream()
                    .map(OrderHistoryItemDto::fromEntity)
                    .toList(); // orderItemDtoList 초기화

            Member member = orderItemList.get(0).getOrder().getMember();
            MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);// memberDto 초기화
            return OrderHistoryDto.fromEntity(myPageMemberDto, orderHistoryItemDtoList);
        } else {
            // 주문 목록이 존재하지 않는 경우
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));
            MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);// memberDto 초기화
            return OrderHistoryDto.fromEntity(myPageMemberDto, null);
        }

    }
}
