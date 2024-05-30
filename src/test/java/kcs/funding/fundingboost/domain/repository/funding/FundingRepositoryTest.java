package kcs.funding.fundingboost.domain.repository.funding;

import static kcs.funding.fundingboost.domain.entity.Tag.BIRTHDAY;
import static kcs.funding.fundingboost.domain.entity.Tag.GRADUATE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;
import kcs.funding.fundingboost.domain.config.QueryDslConfig;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
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
@Import({QueryDslConfig.class})
@Slf4j
class FundingRepositoryTest {

//     @Autowired
//     private FundingRepository fundingRepository;

//     @Autowired
//     private TestEntityManager em;

//     private Member member1, member2, member3;

//     private Funding funding1, funding2;

//     @BeforeEach
//     void setUp() {
//         member1 = Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
//                 "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
//                 46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83");

//         member2 = Member.createMemberWithPoint("구태형", "rnxogud136@gmail.com", "",
//                 "https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg",
//                 999999999, "aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA");

//         member3 = Member.createMemberWithPoint("맹인호", "aoddlsgh98@gmail.com", "",
//                 "https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg",
//                 200000, "aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ");

//         funding1 = Funding.createFunding(member1, "생일축하해주세욥 3월21일입니닷", BIRTHDAY,
//                 LocalDateTime.now().plusDays(15)); // 펀딩 중
//         funding2 = Funding.createFundingForTest(member2, "졸업축하해주세요 사실 졸업 못했어요ㅠㅠ", GRADUATE, 10000,
//                 LocalDateTime.now(), false);//펀딩 종료

//         em.persist(member1);
//         em.persist(member2);
//         em.persist(member3);
//         em.persist(funding1);
//         em.persist(funding2);
//         em.clear();
//     }

//     @Test
//     @DisplayName("findByMemberIdAndStatus: 맴버 아이디와 펀딩상태로 펀딩 조회")
//     void testFindByMemberIdAndStatus_맴버아이디와펀딩상태존재() {
//         // given

//         // when
//         Funding result1 = fundingRepository.findByMemberIdAndStatus(member1.getMemberId(), true)
//                 .orElse(null);
//         Funding result2 = fundingRepository.findByMemberIdAndStatus(member2.getMemberId(), false)
//                 .orElse(null);

//         // then
//         assertThat(result1).isNotNull();
//         assertThat(result2).isNotNull();
//         /**
//          * 맴버
//          */
//         assertThat(result1.getMember().getNickName()).isEqualTo("임창희");
//         assertThat(result2.getMember().getNickName()).isEqualTo("구태형");
//     }

//     @Test
//     @DisplayName("findByMemberIdAndStatus : 해당 맴버의 펀딩이 아예 없을 경우 null 반환")
//     void testFindByMemberIdAndStatusNotFunding_맴버의펀딩이없는경우() {
//         //given

//         //when - 펀딩 샅애 두개 조회
//         Funding result1 = fundingRepository.findByMemberIdAndStatus(member3.getMemberId(), true).orElse(null);
//         Funding result2 = fundingRepository.findByMemberIdAndStatus(member3.getMemberId(), false).orElse(null);
//         //then
//         assertThat(result1).isNull();
//         assertThat(result2).isNull();
//     }

//     @Test
//     @DisplayName("findByMemberIdAndStatus: 해당 맴버의 펀딩이 상태가 다른 걸 조회할 경우 null 반환")
//     void testFindByMemberIdAndStatusWrongStatus_해당펀딩상태가없는걸조회() {

//         //given
//         //when
//         Funding result1 = fundingRepository.findByMemberIdAndStatus(member2.getMemberId(), true).orElse(null);
//         //then
//         assertThat(result1).isNull();
//     }

//     @Test
//     @DisplayName("findMemberById(): 펀딩 존재시 정상적인 Funding return")
//     void testFindMemberById_펀딩존재() {
//         //given

//         //when
//         Funding result1 = fundingRepository.findMemberById(funding1.getFundingId());
//         //then
//         assertThat(result1).isNotNull();
//         assertThat(result1.getTag()).isEqualTo(BIRTHDAY);
//         assertThat(result1.getMember().getNickName()).isEqualTo("임창희");
//     }

//     @Test
//     @DisplayName("findMemberById(): 펀딩 존재 하지 않을시 null값 return")
//     void testFindMemberById_펀딩이존재하지않음() {
//         //when
//         Funding result1 = fundingRepository.findMemberById(3718476283764L);
//         //then
//         assertThat(result1).isNull();
//     }

//     @Test
//     @DisplayName("findFundingInfo: 펀딩 존재시 맴버 아이디를 통해 정상적으로 펀딩 정보 불러오기")
//     void testFindFundingInfo_펀딩존재() {
//         //given
//         Item item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
//                 "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
//                 "샤넬", "뷰티", "00:00");
//         em.persist(item);
//         FundingItem fundingItem = FundingItem.createFundingItem(funding1, item, 1);

//         em.persist(fundingItem);
//         //when
//         Funding result = fundingRepository.findFundingInfo(member1.getMemberId()).orElse(null);
//         //then
//         assertThat(result).isNotNull();
//         assertThat(result.getTag()).isEqualTo(BIRTHDAY);
//         assertThat(result.getMember().getNickName()).isEqualTo("임창희");
//         assertThat(result.getFundingItems().get(0).getItem().getItemName()).isEqualTo(
//                 "NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션");
//     }

//     @Test
//     @DisplayName("findFundingInfo : 해당 맴버가 갖고있는 펀딩이 존재하지 않을 때 null 반환")
//     void testFindFundingInfo_맴버가펀딩이없는경우() {
//         //when
//         Funding result = fundingRepository.findFundingInfo(member3.getMemberId()).orElse(null);
//         //then
//         assertThat(result).isNull();
//     }

//     @Test
//     @DisplayName("findFundingInfo: 맴버가 진행중인 펀딩이 존재하지않을 때 null 반환")
//     void testFindFundingInfo_진행중인펀딩이없는경우() {
//         //when
//         Funding result = fundingRepository.findFundingInfo(member2.getMemberId()).orElse(null);
//         //then
//         assertThat(result).isNull();
//     }

//     @Test
//     @DisplayName("findFundingByMemberId : 지난 펀딩내역 조회시 지난 펀딩들이 존재할 때")
//     void testFindFundingByMemberId_지난펀딩존재시() {
//         //given
//         Item item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
//                 "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
//                 "샤넬", "뷰티", "00:00");
//         FundingItem fundingItem = FundingItem.createFundingItem(funding2, item, 1);

//         em.persist(item);
//         em.persist(fundingItem);
//         //when
//         List<Funding> results = fundingRepository.findFundingByMemberId(member2.getMemberId());
//         //then
//         assertThat(results).isNotNull();
//         assertThat(results.get(0).getTag()).isEqualTo(GRADUATE);
//         assertThat(results.get(0).getMember().getNickName()).isEqualTo("구태형");
//         assertThat(results.get(0).getFundingItems().get(0).getItem().getItemName()).isEqualTo(
//                 "NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션");
//     }

//    @Test
//    @DisplayName("findFundingByMemberId: 현재펀딩 내역만 존재하고 지난펀딩 내역이 존재하지 않을 시 null값 반환")
//    void testFindFundingByMemberId_현재펀딩만존재시() {
//        //given
//        Item item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
//                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
//                "샤넬", "뷰티", "00:00");
//        em.persist(item);
//        FundingItem fundingItem = FundingItem.createFundingItem(funding1, item, 1);
//        System.out.println("fundingStatus : "+funding1.isFundingStatus());
//        em.persist(fundingItem);
//
//        //when
//        List<Funding> results = fundingRepository.findFundingByMemberId(member1.getMemberId());
//        System.out.println("------------"+results.get(0).getFundingId());
//        //then
//        assertThat(results.get(0)).isEqualTo(funding1);
//    }

    @Test
    @DisplayName("findFundingByMemberId: 펀딩내역이 아예 존재하지 않을 시 null값 반환")
    void testFindFundingByMemberId_펀딩이아예없는경우() {
        //when
        List<Funding> results = fundingRepository.findFundingByMemberId(member3.getMemberId());
        //then
        assertThat(results).isEmpty();
    }


}
