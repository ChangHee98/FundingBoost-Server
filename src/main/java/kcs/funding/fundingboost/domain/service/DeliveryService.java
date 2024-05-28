package kcs.funding.fundingboost.domain.service;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.NOT_FOUND_MEMBER;

import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryManageDto;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;
import kcs.funding.fundingboost.domain.repository.DeliveryRepository;
import kcs.funding.fundingboost.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final MemberRepository memberRepository;

    public MyPageDeliveryManageDto getMyDeliveryManageList(Long memberId) {
        List<Delivery> deliveryList = deliveryRepository.findAllByMemberId(memberId);

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CommonException(NOT_FOUND_MEMBER));

        MyPageMemberDto myPageMemberDto = MyPageMemberDto.fromEntity(member);

        List<MyPageDeliveryDto> myPageDeliveryDtoList = deliveryList.stream()
                .map(MyPageDeliveryDto::fromEntity).toList();

        return MyPageDeliveryManageDto.fromEntity(myPageMemberDto, myPageDeliveryDtoList);
    }
}
