package kcs.funding.fundingboost.domain.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;
import kcs.funding.fundingboost.domain.dto.response.myPage.MyPageMemberDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryDto;
import kcs.funding.fundingboost.domain.dto.response.myPage.deliveryManage.MyPageDeliveryManageDto;
import kcs.funding.fundingboost.domain.entity.member.Member;
import kcs.funding.fundingboost.domain.model.DeliveryFixture;
import kcs.funding.fundingboost.domain.model.MemberFixture;
import kcs.funding.fundingboost.domain.model.SecurityContextHolderFixture;
import kcs.funding.fundingboost.domain.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryService deliveryService;

    private Member member;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        member = MemberFixture.member1();
        SecurityContextHolderFixture.setContext(member);
    }

    @DisplayName("배송지 관리 조회")
    @Test
    void viewMyDeliveryManagement() throws Exception {
        // given
        MyPageMemberDto myPageMemberDto = new MyPageMemberDto(member.getNickName(), member.getEmail(),
                member.getProfileImgUrl(), member.getPoint());
        List<MyPageDeliveryDto> myPageDeliveryDtoList = Collections.singletonList(
                MyPageDeliveryDto.fromEntity(DeliveryFixture.address1(member)));

        MyPageDeliveryManageDto expectedResponse = MyPageDeliveryManageDto.fromEntity(myPageMemberDto,
                myPageDeliveryDtoList);
        given(deliveryService.getMyDeliveryManageList(member.getMemberId())).willReturn(expectedResponse);

        // when & then
        mockMvc.perform(get("/api/v1/delivery")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.myPageMemberDto.nickName").value(myPageMemberDto.nickName()))
                .andExpect(jsonPath("$.data.myPageMemberDto.email").value(myPageMemberDto.email()))
                .andExpect(jsonPath("$.data.myPageMemberDto.profileImgUrl").value(myPageMemberDto.profileImgUrl()))
                .andExpect(jsonPath("$.data.myPageMemberDto.point").value(myPageMemberDto.point()))
                .andExpect(jsonPath("$.data.myPageDeliveryDtoList", hasSize(myPageDeliveryDtoList.size())))
                .andExpect(jsonPath("$.data.myPageDeliveryDtoList[0].customerName").value(
                        myPageDeliveryDtoList.get(0).customerName()))
                .andExpect(jsonPath("$.data.myPageDeliveryDtoList[0].address").value(
                        myPageDeliveryDtoList.get(0).address()))
                .andExpect(jsonPath("$.data.myPageDeliveryDtoList[0].phoneNumber").value(
                        myPageDeliveryDtoList.get(0).phoneNumber()));
    }
}