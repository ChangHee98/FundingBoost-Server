// package kcs.funding.fundingboost.init;

// import jakarta.annotation.PostConstruct;
// import jakarta.persistence.EntityManager;
// import java.time.LocalDateTime;
// import java.util.Arrays;
// import java.util.List;
// import kcs.funding.fundingboost.domain.entity.Bookmark;
// import kcs.funding.fundingboost.domain.entity.Contributor;
// import kcs.funding.fundingboost.domain.entity.Delivery;
// import kcs.funding.fundingboost.domain.entity.Funding;
// import kcs.funding.fundingboost.domain.entity.FundingItem;
// import kcs.funding.fundingboost.domain.entity.GiftHubItem;
// import kcs.funding.fundingboost.domain.entity.Item;
// import kcs.funding.fundingboost.domain.entity.Order;
// import kcs.funding.fundingboost.domain.entity.OrderItem;
// import kcs.funding.fundingboost.domain.entity.Relationship;
// import kcs.funding.fundingboost.domain.entity.Tag;
// import kcs.funding.fundingboost.domain.entity.member.Member;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;


// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class InitH2DB {

//     private final InitService initService;

//     /**
//      * 1. member 임(1), 구(2), 맹(3), 양(4), 이(5), 현(6) 2. items Beauty 10개 3. delivery 임창희(id=1) 배송지 2개(id=0, id=1)
//      * 구태형(id=2) 배송지 1개(id=2) 4. order 1 : 임창희, 배송지1 -> order(id=0) 2 : 구태형, 배송2 -> order(id=1) 5. funding & gifthub
//      * ========================================================== * member1(임창희관련) 초기화 * 장바구니 2개 추가 * item1, 임창희 ->
//      * giftHubItem1 * item2, 임창희 -> giftHubItem2 * * 펀딩 2개 추가 * 임창희 -> funding1 * 임창희 -> funding3 * * 펀딩 아이템 2개 추가 *
//      * item1, funding1 -> fundingItem1 * item2, funding1 -> fundingItem2 * * item1, funding3 -> fundingItem3 * item2,
//      * funding3 -> fundingItem4 =================== * member2(구태형 관련) 초기화 * * 펀딩 2개 추가 * 구태형 -> funding2 * 구태형 ->
//      * funding4 * * 펀딩 아이템 2개 추가 * item1, funding4 -> fundingItem5 * item2, funding6 -> fundingItem6
//      * ========================================================== 6. relationship 구태형 - 임창희 친구 관계 설정 ->
//      * relationship(id=0) 7. contributor
//      */
//     @PostConstruct
//     public void initDatabase() {
//         List<Member> members = initService.initMember();
//         List<Item> items = initService.initBeauty();
//         initService.initFashion();
//         initService.initFood();
//         initService.initDigital();
//         initService.initLivingAndBooks();
//         initService.initSports();
// //        items
//         List<Delivery> deliveries = initService.initDelivery(members);
//         initService.initOrders(members, items, deliveries);
//         List<Funding> fundings = initService.initFunding(members, items);

//         initService.initRelationships(members);
//         initService.initContributor(members);
//         initService.initBookmark(members, items);
//     }

//     @Component
//     @Transactional
//     @RequiredArgsConstructor
//     @Slf4j
//     static class InitService {

//         private final EntityManager em;

//         private Item item1;
//         private Item item2;
//         private Item item3;
//         private Item item4;
//         private Item item5;

//         private GiftHubItem giftHubItem1;
//         private GiftHubItem giftHubItem2;
//         private GiftHubItem giftHubItem3;
//         private GiftHubItem giftHubItem4;
//         private GiftHubItem giftHubItem5;
//         private GiftHubItem giftHubItem6;
//         private GiftHubItem giftHubItem7;
//         private GiftHubItem giftHubItem8;
//         private GiftHubItem giftHubItem9;
//         private GiftHubItem giftHubItem10;

//         private Member member1;
//         private Member member2;
//         private Member member3;
//         private Member member4;
//         private Member member5;
//         private Member member6;
//         private Member member7;
//         private Member member8;
//         private Member member9;
//         private Member member10;

//         private Delivery delivery1;
//         private Delivery delivery2;

//         private Funding funding1;
//         private Funding funding2;
//         private Funding funding3;
//         private Funding funding4;
//         private Funding funding5;
//         private Funding funding6;
//         private Funding funding7;
//         private Funding funding8;
//         private Funding funding9;
//         private Funding funding10;

//         private FundingItem fundingItem1;
//         private FundingItem fundingItem2;
//         private FundingItem fundingItem3;
//         private FundingItem fundingItem4;

//         private Bookmark bookmark1;
//         private Bookmark bookmark2;
//         private Bookmark bookmark3;
//         private Bookmark bookmark4;
//         private Bookmark bookmark5;


//         public void initOrders(List<Member> members, List<Item> items, List<Delivery> deliveries) {
//             item1 = items.get(0);
//             item2 = items.get(1);

//             member1 = members.get(0);
//             member2 = members.get(0);

//             delivery1 = deliveries.get(0);
//             delivery2 = deliveries.get(1);

//             Order order1 = Order.createOrder(member1, delivery1);
//             Order order2 = Order.createOrder(member2, delivery2);
//             em.persist(order1);
//             em.persist(order2);

//             OrderItem orderItem1 = OrderItem.createOrderItem(order1, item1, 1);
//             OrderItem orderItem2 = OrderItem.createOrderItem(order1, item2, 2);
//             em.persist(orderItem1);
//             em.persist(orderItem2);
//         }

//         public List<Delivery> initDelivery(List<Member> members) {
//             member1 = members.get(0);
//             member2 = members.get(1);

//             List<Delivery> deliveryInfos = Arrays.asList(
//                     Delivery.createDelivery("서울 금천구 가산디지털1로 189 (주)LG 가산 디지털센터 12층", "010-1111-2222",
//                             "직장", member1),
//                     Delivery.createDelivery("경기도 화성시", "010-3333-4444", "집",
//                             member1),
//                     Delivery.createDelivery("경기도 성남시 분당구 판교역로 166 카카오 아지트", "010-1234-5678", "사무실",
//                             member2));

//             for (Delivery delivery : deliveryInfos) {
//                 em.persist(delivery);
//             }
//             return deliveryInfos;
//         }

//         public List<Funding> initFunding(List<Member> members, List<Item> items) {
//             /**
//              * member1(임창희 관련) 초기화
//              *
//              * 장바구니 2개 추가
//              * item1, 임창희 -> giftHubItem1
//              * item2, 임창희 -> giftHubItem2
//              *
//              * 펀딩 2개 추가
//              * 임창희 -> funding1
//              * 임창희 -> funding3
//              *
//              * 펀딩 아이템 2개 추가
//              * item1, funding1 -> fundingItem1
//              * item2, funding1 -> fundingItem2
//              *
//              * item1, funding3 -> fundingItem3
//              * item2, funding3 -> fundingItem4
//              */
//             member1 = members.get(0);
//             member2 = members.get(1);
//             member3 = members.get(2);
//             member4 = members.get(3);
//             member5 = members.get(4);
//             member6 = members.get(5);
//             member7 = members.get(6);
//             member8 = members.get(7);
//             member9 = members.get(8);
//             member10 = members.get(9);

//             item1 = items.get(0);
//             item2 = items.get(1);
//             item3 = items.get(2);
//             item4 = items.get(3);
//             item5 = items.get(4);

//             for (Item item : items) {
//                 giftHubItem1 = GiftHubItem.createGiftHubItem(1, item, member1);
//                 giftHubItem2 = GiftHubItem.createGiftHubItem(1, item, member2);
//                 giftHubItem3 = GiftHubItem.createGiftHubItem(1, item, member5);
//                 giftHubItem4 = GiftHubItem.createGiftHubItem(1, item, member6);
//                 giftHubItem5 = GiftHubItem.createGiftHubItem(1, item, member7);
//                 giftHubItem6 = GiftHubItem.createGiftHubItem(1, item, member8);
//                 giftHubItem7 = GiftHubItem.createGiftHubItem(1, item, member9);
//                 giftHubItem8 = GiftHubItem.createGiftHubItem(1, item, member10);
//                 giftHubItem9 = GiftHubItem.createGiftHubItem(1, item, member3);
//                 giftHubItem10 = GiftHubItem.createGiftHubItem(1, item, member4);
//                 em.persist(giftHubItem1);
//                 em.persist(giftHubItem2);
//                 em.persist(giftHubItem3);
//                 em.persist(giftHubItem4);
//                 em.persist(giftHubItem5);
//                 em.persist(giftHubItem6);
//                 em.persist(giftHubItem7);
//                 em.persist(giftHubItem8);
//                 em.persist(giftHubItem9);
//                 em.persist(giftHubItem10);
//             }

//             funding1 = Funding.createFunding(member1, "1번 생일축하해줘",
//                     Tag.BIRTHDAY, LocalDateTime.now().plusDays(14));
//             funding2 = Funding.createFunding(member2, "2번 생일 축하~",
//                     Tag.BIRTHDAY, LocalDateTime.now().plusDays(14));
//             funding3 = Funding.createFunding(member5, "5번이 펀딩을 만들었다.",
//                     Tag.ETC, LocalDateTime.now().plusDays(14));
//             funding4 = Funding.createFunding(member6, "6번이 펀딩을 만들었다.",
//                     Tag.GRADUATE, LocalDateTime.now().plusDays(14));
//             funding5 = Funding.createFunding(member7, "7번이 펀딩을 만들었다.",
//                     Tag.ETC, LocalDateTime.now().plusDays(14));
//             funding6 = Funding.createFunding(member8, "8번 펀딩",
//                     Tag.GRADUATE, LocalDateTime.now().plusDays(14));
//             funding7 = Funding.createFunding(member9, "9번 펀딩",
//                     Tag.ETC, LocalDateTime.now().plusDays(14));
//             funding8 = Funding.createFunding(member10, "10번 펀딩",
//                     Tag.BIRTHDAY, LocalDateTime.now().plusDays(14));
//             funding9 = Funding.createFunding(member3, "3번 펀딩",
//                     Tag.BIRTHDAY, LocalDateTime.now().plusDays(14));
//             funding10 = Funding.createFunding(member4, "4번 펀딩",
//                     Tag.BIRTHDAY, LocalDateTime.now().plusDays(14));

//             em.persist(funding1);
//             em.persist(funding2);
//             em.persist(funding3);
//             em.persist(funding4);
//             em.persist(funding5);
//             em.persist(funding6);
//             em.persist(funding7);
//             em.persist(funding8);
//             em.persist(funding9);
//             em.persist(funding10);

//             fundingItem1 = FundingItem.createFundingItem(funding1, item1, 1);
//             fundingItem2 = FundingItem.createFundingItem(funding1, item2, 2);
//             fundingItem3 = FundingItem.createFundingItem(funding1, item3, 3);
//             fundingItem4 = FundingItem.createFundingItem(funding1, item4, 4);
//             em.persist(fundingItem1);
//             em.persist(fundingItem2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             fundingItem3 = FundingItem.createFundingItem(funding2, item1, 1);
//             fundingItem4 = FundingItem.createFundingItem(funding2, item2, 2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             fundingItem1 = FundingItem.createFundingItem(funding3, item3, 1);
//             fundingItem2 = FundingItem.createFundingItem(funding3, item4, 2);
//             fundingItem3 = FundingItem.createFundingItem(funding3, item2, 3);
//             em.persist(fundingItem1);
//             em.persist(fundingItem2);
//             em.persist(fundingItem3);

//             fundingItem1 = FundingItem.createFundingItem(funding4, item3, 1);
//             fundingItem2 = FundingItem.createFundingItem(funding4, item4, 2);
//             fundingItem3 = FundingItem.createFundingItem(funding4, item5, 3);
//             em.persist(fundingItem1);
//             em.persist(fundingItem2);
//             em.persist(fundingItem3);

//             fundingItem1 = FundingItem.createFundingItem(funding5, item3, 1);
//             fundingItem2 = FundingItem.createFundingItem(funding5, item4, 2);
//             fundingItem3 = FundingItem.createFundingItem(funding5, item5, 3);
//             em.persist(fundingItem1);
//             em.persist(fundingItem2);
//             em.persist(fundingItem3);

//             fundingItem1 = FundingItem.createFundingItem(funding6, item3, 1);
//             fundingItem2 = FundingItem.createFundingItem(funding6, item4, 2);
//             fundingItem3 = FundingItem.createFundingItem(funding6, item5, 3);
//             em.persist(fundingItem1);
//             em.persist(fundingItem2);
//             em.persist(fundingItem3);

//             fundingItem3 = FundingItem.createFundingItem(funding7, item1, 1);
//             fundingItem4 = FundingItem.createFundingItem(funding7, item2, 2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             fundingItem3 = FundingItem.createFundingItem(funding8, item1, 1);
//             fundingItem4 = FundingItem.createFundingItem(funding8, item2, 2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             fundingItem3 = FundingItem.createFundingItem(funding9, item1, 1);
//             fundingItem4 = FundingItem.createFundingItem(funding9, item2, 2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             fundingItem3 = FundingItem.createFundingItem(funding10, item1, 1);
//             fundingItem4 = FundingItem.createFundingItem(funding10, item2, 2);
//             em.persist(fundingItem3);
//             em.persist(fundingItem4);

//             return List.of(funding1, funding2, funding3, funding4, funding5,
//                     funding6, funding7, funding8,
//                     funding9, funding10);
//         }

//         public void initRelationships(List<Member> members) {
//             member1 = members.get(0);
//             member2 = members.get(1);
//             member3 = members.get(2);
//             member5 = members.get(4);
//             member6 = members.get(5);
//             member7 = members.get(6);
//             member8 = members.get(7);
//             member9 = members.get(8);
//             member10 = members.get(9);

//             List<Relationship> relationshipList = Relationship.createRelationships(member1, member2);
//             List<Relationship> relationshipList1 = Relationship.createRelationships(member1, member3);
//             List<Relationship> relationshipList2 = Relationship.createRelationships(member1, member4);
//             List<Relationship> relationshipList3 = Relationship.createRelationships(member1, member5);
//             List<Relationship> relationshipList4 = Relationship.createRelationships(member1, member6);
//             List<Relationship> relationshipList5 = Relationship.createRelationships(member1, member7);
//             List<Relationship> relationshipList6 = Relationship.createRelationships(member1, member8);
//             List<Relationship> relationshipList7 = Relationship.createRelationships(member1, member9);
//             List<Relationship> relationshipList8 = Relationship.createRelationships(member1, member10);

//             for (int i = 0; i < relationshipList.size(); i++) {
//                 em.persist(relationshipList.get(i));
//                 em.persist(relationshipList1.get(i));
//                 em.persist(relationshipList2.get(i));
//                 em.persist(relationshipList3.get(i));
//                 em.persist(relationshipList4.get(i));
//                 em.persist(relationshipList5.get(i));
//                 em.persist(relationshipList6.get(i));
//                 em.persist(relationshipList7.get(i));
//                 em.persist(relationshipList8.get(i));
//             }
//         }

//         public List<Member> initMember() {
//             List<Member> memberInfos = Arrays.asList(
//                     Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com",
//                             "{bcrypt}$2a$10$bIBTxCY.RFIcDncT8spdEOPImxovk626iI6FzCzduXGIpxvwAen0i",
//                             "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
//                             46000, "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83"),
//                     Member.createMemberWithPoint("구태형", "rnxogud136@gmail.com", "",
//                             "https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg",
//                             999999999, "aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA"),
//                     Member.createMemberWithPoint("맹인호", "aoddlsgh99@gmail.com", "",
//                             "https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg",
//                             200000, "aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ"),
//                     Member.createMemberWithPoint("양혜인", "helen66626662@gmail.com", "",
//                             "https://p.kakaocdn.net/th/talkp/woGALKKcHt/jiOhwZDs9RTkkXPwNYjxF1/wzruf2_110x110_c.jpg",
//                             300000, "aFtpXm1ZaVtuQnRMeUp9Tn5PY1JiV2JRaF8z"),
//                     Member.createMemberWithPoint("이재현", "jhyun030299@gmail.com", "",
//                             "https://k.kakaocdn.net/dn/jrT50/btsF9BGMPni/7oxQfq58KmKxIl8UX01mn0/img_110x110.jpg",
//                             400000, "aFpqUmZVYFRsQHFIfU53R3ZDdlprW25baFFmDw"),
//                     Member.createMemberWithPoint("현세미", "abc123@gmail.com", "", "",
//                             500000, "..."),
//                     Member.createMemberWithPoint("손흥민", "abc1234@gmail.com", "", "",
//                             0, "..."),
//                     Member.createMemberWithPoint("박재범", "aaa123@gmail.com", "", "",
//                             0, "..."),
//                     Member.createMemberWithPoint("봉준호", "aaa1234@gmail.com", "", "",
//                             0, "..."),
//                     Member.createMemberWithPoint("싸이", "bbb123@gmail.com", "", "",
//                             0, "...")
//             );

//             for (Member member : memberInfos) {
//                 em.persist(member);
//             }
//             return memberInfos;
//         }

//         public void initContributor(List<Member> members) {
//             member3 = members.get(2);
//             member4 = members.get(3);
//             member5 = members.get(4);
//             member6 = members.get(5);
//             member7 = members.get(6);
//             member8 = members.get(7);
//             member9 = members.get(8);
//             member10 = members.get(9);

//             Contributor contributor1 = Contributor.createContributor(10000, member3, funding1);
//             Contributor contributor2 = Contributor.createContributor(10000, member4, funding1);
//             Contributor contributor3 = Contributor.createContributor(10000, member5, funding1);
//             Contributor contributor4 = Contributor.createContributor(10000, member6, funding1);
//             Contributor contributor5 = Contributor.createContributor(10000, member7, funding1);
//             Contributor contributor6 = Contributor.createContributor(10000, member8, funding1);
//             Contributor contributor7 = Contributor.createContributor(10000, member9, funding1);
//             Contributor contributor8 = Contributor.createContributor(10000, member10, funding1);
//             em.persist(contributor1);
//             em.persist(contributor2);
//             em.persist(contributor3);
//             em.persist(contributor4);
//             em.persist(contributor5);
//             em.persist(contributor6);
//             em.persist(contributor7);
//             em.persist(contributor8);
//             em.merge(funding1);
//         }

//         public List<Item> initBeauty() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
//                             "샤넬", "뷰티", "00:00"),

//                     Item.createItem("NEW 루쥬 코코 밤(+샤넬 기프트 카드)", 51000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg",
//                             "샤넬", "뷰티", "934 코랄린 [NEW]"),

//                     Item.createItem("코코 마드모아젤 헤어 미스트 35ml", 85000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg",
//                             "샤넬", "뷰티", "코코 마드모아젤 헤어 미스트 35ml"),

//                     Item.createItem("레 베쥬 립 밤", 85000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220316102858_5380e2c951fc4661a0ec7b08a0bc96ee.jpg",
//                             "샤넬", "뷰티", "미디엄"),

//                     Item.createItem("[단독+각인+포장] 입생로랑 1위 NEW 벨벳 틴트 세트(+리브르 향수 1.2ml)", 49000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327134440_58d0d7e4b2ea4baebae1b4a3c065198c.jpg",
//                             "입생로랑", "뷰티", "220 컨트롤 블러시 (NEW - 로지 코랄)"),

//                     Item.createItem("[선물포장] 리브르 핸드 크림 30ml", 33000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240415092310_78cc616347524a47b0cbc3f799a6193b.jpg",
//                             "입생로랑", "뷰티", "[선물포장] 리브르 핸드 크림 30ml"),

//                     Item.createItem("[각인+포장] NEW 엉크르 드 뽀 쿠션 세트(+라운드 파우치)", 108000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327140332_02835b48b1bc4faeaaaaad51f48aa62b.jpg",
//                             "입생로랑", "뷰티", "10호"),

//                     Item.createItem("[단독/리미티드선물포장] 코롱 9ML", 32000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411173509_791cf65118c142c68621829f4e2a42df.jpg",
//                             "조말론런던", "뷰티", "블랙베리 앤 베이 코롱 9ML"),

//                     Item.createItem("[선물포장] 바디 크림 50ML", 45000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240315092736_2ffa91bc2e0d4430bb0fa69db5d2a431.jpg",
//                             "조말론런던", "뷰티", "블랙베리 앤 베이 바디 크림 50ML"),

//                     Item.createItem("[단독각인/슬리브선물포장] 코롱 30ML", 110000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411170225_64f314ccd05a4570a90add7ee46480b4.jpg",
//                             "조말론런던", "뷰티", "블랙베리 앤 베이 코롱 30ML"));

//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public List<Item> initFashion() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("티파니 T1 와이드 힌지드 뱅글", 11100000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20201113163447_fa637a9163a8446db21c029e41fe0c4b.jpg",
//                             "티파니앤코", "패션", "티파니 T1 와이드 힌지드 뱅글(미디움)"),
//                     Item.createItem("티파니 T1 스몰 써클 펜던트 (18K 로즈골드)", 5500000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230607184420_2be0f56fd7e64cbf873bc195ed8b7a78.jpg",
//                             "티파니앤코", "패션", "티파니 T1 스몰 써클 펜던트 (18K 로즈골드)"),
//                     Item.createItem("티파니 T 스마일 펜던트 (스몰, 18K 옐로우 골드)", 1700000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240329163232_e0b5b7650718467b9dd34752f814aba9.jpg",
//                             "티파니앤코", "패션", "티파니 T 스마일 펜던트 (스몰)"),
//                     Item.createItem("[단독] 사토리얼 5cc 카드 지갑 더스티 블루 198245", 320000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131150315_dc352197be5f4225b76422834e5eba9b.jpg",
//                             "몽블랑", "패션", "[단독] 사토리얼 5cc 카드 지갑 더스티 블루 198245"),
//                     Item.createItem("사토리얼 4cc 카드/명함 지갑 클레이 198241", 410000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240227151217_c5cde202c68c40c9bdf452f69634c2a1.jpg",
//                             "몽블랑", "패션", "사토리얼 4cc 카드/명함 지갑 클레이 198241"),
//                     Item.createItem("픽스 블랙 볼펜 132495", 410000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231124162609_ec8347e3c9d948cbad54edfeb6f313de.png",
//                             "몽블랑", "패션", "픽스 블랙 볼펜 132495"),
//                     Item.createItem("사토리얼 5cc 카드 지갑 130324", 32000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230619153421_e3ebb9c1194a42d4bf6d74e64954a91f.jpg",
//                             "몽블랑", "패션", "사토리얼 5cc 카드 지갑 130324"),
//                     Item.createItem("[GG 마몽] 수퍼 미니백", 1890000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240314092155_6690cfb440e04398ba371793ecfafa61",
//                             "구찌", "패션", "[GG 마몽] 수퍼 미니백"),
//                     Item.createItem("구찌 스크립트 카드 케이스", 420000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231114083558_e09df89addd1493ebd7aca3c1ffa9409.jpg",
//                             "구찌", "패션", "구찌 스크립트 카드 케이스"),
//                     Item.createItem("구찌 스크립트 미니 지갑", 900000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231111050644_41e515e818a94bd0b08a777cc24e7533.jpg",
//                             "구찌", "패션", "구찌 스크립트 미니 지갑")
//             );
//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public List<Item> initFood() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("농협안심한우 1등급 소한마리알찬세트 1.35kg (등심250g+채끝200g+장조림300g+불고기300g+국거리300g) 원산지 : 국내산",
//                             94800,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231227090524_1b979e0b92184f37a79b5909c4fb2298.png",
//                             "농협안심한우", "식품", "농협안심한우 1등급 소한마리알찬세트 1.35kg (등심250g+채끝200g+장조림300g+불고기300g+국거리300g)"),
//                     Item.createItem("비단같은 윤기 비단쌀 2kg 원산지 : 국내산", 15700,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230830142538_2aab21d6940a44b1bdd0a38515e535d9.jpg",
//                             "현대쌀집", "식품", "비단같은 윤기 비단쌀 2kg"),
//                     Item.createItem("스타벅스 클래식 넛츠 타르트", 30000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240315134009_48b4dcb12c0a4e1f996cdadee4f60135.jpg",
//                             "스타벅스", "식품", "스타벅스 클래식 넛츠 타르트"),
//                     Item.createItem("[경복궁] 보리굴비 실속세트(보리굴비 290g*4미+보성말차 1box)", 110000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240109175838_70da4296f30f466d8058d18cdb14e1aa.jpg",
//                             "경복궁", "식품", "[경복궁] 보리굴비 실속세트(보리굴비 290g*4미+보성말차 1box)"),
//                     Item.createItem("아프다고 굶지마요, 본죽 14종+장조림 set (총 7팩/10팩)", 19900,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230329110522_d0c199c7b1d3458ea11bdfc6afca4829.jpg",
//                             "본죽식품", "식품", " 02_★BEST★200g전복/쇠고기/낙지김치/보양삼계/단호박+미니 장조림 5팩 "),
//                     Item.createItem("최고급 그리스 엑스트라 버진 올리브오일 3종 선물세트", 123000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230817175251_44a5ff64afde43b1a7f900eedb9141f7.jpg",
//                             "이야이야앤프랜즈", "식품", "최고급 그리스 엑스트라 버진 올리브오일 3종 선물세트"),
//                     Item.createItem("[워커힐호텔]명월관 명품 갈비탕 6팩x600g", 102000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220217100152_9854e082dde34b0baadded69bfabc4f2.jpg",
//                             "워커힐호텔", "식품", "[워커힐호텔]명월관 명품 갈비탕 6팩x600g"),
//                     Item.createItem("[보자기증정] 고급 과일바구니, 카멜 2호 10종 6.8kg이상(멜론,애플망고)", 139800,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240223161135_1fa6ffa4fde74bb786eefd39964b989d.jpg",
//                             "온브릭스", "식품", "[보자기증정] 고급 과일바구니, 카멜 2호 10종 6.8kg이상(멜론,애플망고)"),
//                     Item.createItem("TWG Tea BON VOYAGE 티백 디카페인 3종세트 (3개 골라담기)", 111000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231003221734_aaeb9a411e074a949ea76e036fefd658.jpg",
//                             "TWG Tea(LuX)", "식품", "미드나이트 아워 티"),
//                     Item.createItem("와인 치즈 플래터 11종(무알콜와인+치즈+샤퀴테리+크래커+올리브+캐비어) 스파클링와인 와인안주 생일 결혼기념일 집들이 혼술", 199800,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213153404_b9db7167008942c590f09d5bf9a8a91f.jpg",
//                             "델리스", "식품", "와인 치즈 플래터 11종(무알콜와인+치즈+샤퀴테리+크래커+올리브+캐비어) 스파클링와인 와인안주 생일 결혼기념일 집들이 혼술")
//             );
//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public List<Item> initDigital() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("[카카오 단독] [김희선 PICK] 등기기 부스터 프로 (파우치&리프팅크림 증정)", 339000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240408205352_b011db2c5cd642faaadb3f62b806c59d.jpg",
//                             "메디큐브 에이지알", "디지털", "본품"),
//                     Item.createItem("Apple 에어팟 프로 2세대 USB-C 타입 (MTJV3KH/A)", 51000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231012202825_9cdbe5b636ae4f2e9dfcec7ef6af1bc4.jpg",
//                             "Apple", "디지털", "에어팟 프로 2세대(C타입) MTJV3KH/A"),
//                     Item.createItem("Apple 아이폰 15 128GB 자급제", 1250000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230920133803_e7c2b1be1e1942a88d503016d0222b4e.jpg",
//                             "Apple", "디지털", "블랙"),
//                     Item.createItem("[현대백화점] 라이라복스 칼로타 하이엔드 액티브 스피커 Lyravox Karlotta", 43900000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FOPwECGRDycdUbcRBDOeH1g%2FVH7v13Fcmw9mgjyw095DWFt1sT2xxT6pW_p6e-AxhUk.jpg",
//                             "오드", "디지털", "라이라복스 칼로타 하이엔드 액티브 스피커 Lyravox Karlotta"),
//                     Item.createItem("삼성 BESPOKE 무풍에어컨 갤러리 청정 홈멀티 홈멀티/ 기본설치비무료 / 삼성물류직송", 6621600,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230315111431_36fb8b3bf1b7478ea1cf1c36b5654ddc.jpg",
//                             "삼성전자", "디지털", "일반배관"),
//                     Item.createItem("쿠쿠 리네이처 컴팩트 안마의자 CMS-G210NW", 3590000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240202144335_eef562abafe645839fc8316a191bef79.jpg",
//                             "쿠쿠", "디지털", "쿠쿠 리네이처 컴팩트 안마의자 CMS-G210NW"),
//                     Item.createItem("LG디오스 식기세척기+전기레인지 DUBJ2GAL+BEI3GQUO (DUBJ2GAL + BEI3GQUO)", 3000000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230620153643_326272ab17a94ed8bed41ae13280cbe2.jpg",
//                             "LG전자", "디지털", "LG디오스 식기세척기+전기레인지 DUBJ2GAL+BEI3GQUO"),
//                     Item.createItem("다이슨 무선청소기 V15 디텍트(골드/골드)", 1290000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FSFSDxyVdWVoJ4lb06M2p-Q%2FluWBima0QGAHou-f3EwL8kbFwbP3QTtbIIdjdGiqgUI.jpg",
//                             "다이슨(일반)", "디지털", "다이슨 무선청소기 V15 디텍트(골드/골드)"),
//                     Item.createItem("[당일발송] 가전필수템 아이닉 올인원 로봇청소기 iX10", 899000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230323142713_72b6769f880d47e29de5db2ada395fe9.jpg",
//                             "아이닉", "디지털", "iX10 로봇청소기 화이트"),
//                     Item.createItem("[PS5] PlayStation5 슬림 디지털 에디션 (플레이스테이션 5 825GB)", 558000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231219095942_942598d5ce424fef96953ffafee2cb63.jpg",
//                             "소니플레이스테이션", "디지털", "[PS5] PlayStation5 슬림 디지털 에디션")
//             );
//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public List<Item> initLivingAndBooks() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("선물하기 좋아요 그린 or 크림 스탠리 켄처 텀블러 +에코텀블러음료쿠폰(MMS발송)", 130000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231227152358_f775b058619c4c4fa26d0f535f9b8468.jpg",
//                             "스타벅스", "리빙/도서", "SS 스탠리 그린 컨처 텀블러 591ml"),
//                     Item.createItem("STELI 스텔리 무드등 다이닝 침실 스탠드 조명 - LH95", 114000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231110133124_488019c472804206a0a044e52af2606a.jpg",
//                             "렉슨", "리빙/도서", "골드(DOME) - LH95D-D"),
//                     Item.createItem("우스터 1인 전동 리클라이너_브라운", 1590000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FLnldKldJtt3gFKjSd4jGPg%2FE2DlYb6apHJJJ0uwBa26J1106e9aUwKVGhiNEqtVt9o.jpg",
//                             "까사미아", "리빙/도서", "브라운"),
//                     Item.createItem("[에이스침대]BRA 1441-E AT등급/SS(슈퍼싱글사이즈)", 1592000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FDCRNEgQye1j6Xm6ht9v_lA%2FJ_PfBm-1icT6RjHMIyxLDFZCOHUtAf85V_z6-56owdQ.jpg",
//                             "에이스침대", "리빙/도서", "라이트그레이"),
//                     Item.createItem("아벨리아 거실장세트4(단문장+거실장1500+협탁)", 1547000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FwlbUrJzbiq_LAha_qm-iBQ%2FMQMwppXn3E13sA4xCVMEo_TPOmTY5U0PiO2mC-sRxFg.jpg",
//                             "인터데코", "리빙/도서", ""),
//                     Item.createItem("[NOMON] DARO 노몬 다로", 1525100,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210930150604_d308a97c287147b0a2955ea422189bc9.jpg",
//                             "노먼", "리빙/도서", ""),
//                     Item.createItem("Artesia Pro 아르테시아 전자드럼 올메쉬 A50 SET2 풀패키지", 1510000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230711093002_594eaada3ba44b1cabae96d8540ca9e6.jpg",
//                             "악기", "뷰티리빙/도서", ""),
//                     Item.createItem("차나 3인용 아쿠아클린 패브릭 소파", 1480000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F3Wbt8mb8Xl32vd40X5sXMA%2F6_aTEbpQadLhvl8F-3lW0VaM47yFyZtzhHeMnIuSgys.jpg",
//                             "봄소와", "리빙/도서", "Alhambra 1"),
//                     Item.createItem("[갤러리아] 아뜨리에 100 폴란드 프리미엄 구스다운 이불솜 K", 1478520,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220708094538_380dbfd23e6f4df8ab87c0fdfea2d732.JPG",
//                             "클라르하임", "리빙/도서", "화이트"),
//                     Item.createItem("[롯데백화점][빅토리녹스 공식] 이녹스 크로노 블루 다이얼 실버 브레이슬릿 시계 241985", 1436500,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2Fy29ROOeKyyftcs8NxEgtAw%2FC4jsd78xw7J6dQhmfBe_tK_7A8yyUIzLR0ambN36Ehc",
//                             "빅토리녹스", "리빙/도서", "블루 다이얼 실버 브레이슬릿")
//             );
//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public List<Item> initSports() {
//             List<Item> itemInfos = Arrays.asList(
//                     Item.createItem("아디다스 런닝머신 T-19i 가정용 유산소 접이식 아파트 워킹 저소음 패드 실내 트레드밀", 2580000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230511154115_da57987fafee42aaac243d397e57afc0.jpg",
//                             "아디다스 피트니스", "스포츠", "아디다스 런닝머신 T-19i 가정용 유산소 접이식 아파트 워킹 저소음 패드 실내 트레드밀"),
//                     Item.createItem("프로기어 PRGR 2024 슈퍼에그 카본 여성 7아이언세트 GC", 2520000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FVXUgMTnWP3QnWT-Q7UWTZA%2F2IAf-Cnk5iv0e8SQP9LHCvSV_QMNbjripQrrFB6JpYU.jpg",
//                             "골프용품", "스포츠", "여성7아이언"),
//                     Item.createItem("코베아 아웃백 시그니처", 1590000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210430124143_09107a814d6a4bfaa9803e1113cf9548.png",
//                             "코베아", "스포츠", "코베아 아웃백 시그니처"),
//                     Item.createItem("AU테크 스카닉 2X 배달용 고출력 전기자전거 48V 15Ah", 879000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230419111854_aabe46a7905f49e0a9420f28b340c69f.jpg",
//                             "AU테크", "스포츠", "블랙 {PH}"),
//                     Item.createItem("여성 손목 시계", 553300,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230508173419_0af838189c854da5b2ef88d56a14cab0.png",
//                             "메르세데스-벤츠", "스포츠", "여성 손목 시계"),
//                     Item.createItem("그랜드 전동거꾸리 물구나무서기 스트레칭 기구", 568000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230502093549_b90c730f0dd648beaa6dc1cccfa79a88.jpg",
//                             "반석스포츠", "스포츠", "그랜드 전동거꾸리 물구나무서기 스트레칭 기구"),
//                     Item.createItem("[나노휠] 전동킥보드 NQ-01 Plus+ 프리미엄 36V (10.4Ah)", 486300,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20221130113512_e49157b0d35b43cca46afc099df0d344.jpg",
//                             "나노휠", "스포츠", "[나노휠] 전동킥보드 NQ-01 Plus+ 프리미엄 36V (10.4Ah)"),
//                     Item.createItem("[골프공 선물 추천] 타이틀리스트 PRO V1 / PRO V1X 골프공", 70000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230203142810_9ce2c3a85e2745618fc9ba46d81203b7.jpg",
//                             "타이틀리스트", "스포츠", "PRO_V1"),
//                     Item.createItem("[단독][BEST/한정수량] 아디다스 미니 에어라이너 백 IL9610", 65000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2Fvw_r47QTKC24nRRgdC6woQ%2FBqOXmZ4Su512SqCWL72DvmCNE4YV_EfBlFd5lWD8rWQ.jpg",
//                             "아디다스코리아", "스포츠", "NS"),
//                     Item.createItem("오센트 스마일리 제주 차량용방향제", 52000,
//                             "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210929082549_98e6e54cce164da5bc9e7e588fc2c232.png",
//                             "오센트", "스포츠", "오센트 스마일리 제주 차량용방향제")
//             );
//             for (Item item : itemInfos) {
//                 em.persist(item);
//             }
//             return itemInfos;
//         }

//         public void initBookmark(List<Member> members, List<Item> items) {

//             member1 = members.get(0);
//             member2 = members.get(1);

//             item1 = items.get(0);
//             item2 = items.get(1);
//             item3 = items.get(2);
//             item4 = items.get(3);
//             item5 = items.get(4);

//             bookmark1 = Bookmark.createBookmark(member1, item1);
//             bookmark2 = Bookmark.createBookmark(member1, item2);
//             bookmark3 = Bookmark.createBookmark(member2, item3);
//             bookmark4 = Bookmark.createBookmark(member2, item4);
//             bookmark5 = Bookmark.createBookmark(member2, item5);

//             em.persist(bookmark1);
//             em.persist(bookmark2);
//             em.persist(bookmark3);
//             em.persist(bookmark4);
//             em.persist(bookmark5);

//         }
//     }
// }