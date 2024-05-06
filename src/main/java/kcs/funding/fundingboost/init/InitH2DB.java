package kcs.funding.fundingboost.init;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Bookmark;
import kcs.funding.fundingboost.domain.entity.Contributor;
import kcs.funding.fundingboost.domain.entity.Delivery;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.entity.Order;
import kcs.funding.fundingboost.domain.entity.OrderItem;
import kcs.funding.fundingboost.domain.entity.Relationship;
import kcs.funding.fundingboost.domain.entity.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
@Slf4j
public class InitH2DB {

    private final InitService initService;

    /**
     * 1. member 임(1), 구(2), 맹(3), 양(4), 이(5), 현(6) 2. items Beauty 10개 3. delivery 임창희(id=1) 배송지 2개(id=0, id=1)
     * 구태형(id=2) 배송지 1개(id=2) 4. order 1 : 임창희, 배송지1 -> order(id=0) 2 : 구태형, 배송2 -> order(id=1) 5. funding & gifthub
     * ========================================================== * member1(임창희관련) 초기화 * 장바구니 2개 추가 * item1, 임창희 ->
     * giftHubItem1 * item2, 임창희 -> giftHubItem2 * * 펀딩 2개 추가 * 임창희 -> funding1 * 임창희 -> funding3 * * 펀딩 아이템 2개 추가 *
     * item1, funding1 -> fundingItem1 * item2, funding1 -> fundingItem2 * * item1, funding3 -> fundingItem3 * item2,
     * funding3 -> fundingItem4 =================== * member2(구태형 관련) 초기화 * * 펀딩 2개 추가 * 구태형 -> funding2 * 구태형 ->
     * funding4 * * 펀딩 아이템 2개 추가 * item1, funding4 -> fundingItem5 * item2, funding6 -> fundingItem6
     * ========================================================== 6. relationship 구태형 - 임창희 친구 관계 설정 ->
     * relationship(id=0) 7. contributor
     */
    @PostConstruct
    public void initDatabase() {
        List<Member> members = initService.initMember();
        List<Item> items = initService.initBeauty();
        List<Delivery> deliveries = initService.initDelivery(members);
        initService.initOrders(members, items, deliveries);
        List<Funding> fundings = initService.initFunding(members, items);
        initService.initRelationships(members);
        initService.initContributor(members, fundings);
        initService.initBookmark(members, items);
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    @Slf4j
    static class InitService {

        private final EntityManager em;

        private Item item1;
        private Item item2;
        private Item item3;
        private Item item4;
        private Item item5;

        private GiftHubItem giftHubItem1;
        private GiftHubItem giftHubItem2;

        private Member member1;
        private Member member2;
        private Member member3;
        private Member member4;
        private Member member5;
        private Member member6;
        private Delivery delivery1;
        private Delivery delivery2;

        private Funding funding1;
        private Funding funding2;
        private Funding funding3;
        private Funding funding4;
        private FundingItem fundingItem1;
        private FundingItem fundingItem2;
        private FundingItem fundingItem3;
        private FundingItem fundingItem4;

        private Bookmark bookmark1;
        private Bookmark bookmark2;
        private Bookmark bookmark3;
        private Bookmark bookmark4;
        private Bookmark bookmark5;


        public void initOrders(List<Member> members, List<Item> items, List<Delivery> deliveries) {
            item1 = items.get(0);
            item2 = items.get(1);

            member1 = members.get(0);
            member2 = members.get(0);

            delivery1 = deliveries.get(0);
            delivery2 = deliveries.get(1);

            Order order1 = Order.createOrder(member1, delivery1);
            Order order2 = Order.createOrder(member2, delivery2);
            em.persist(order1);
            em.persist(order2);

            OrderItem orderItem1 = OrderItem.createOrderItem(order1, item1, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(order1, item2, 2);
            em.persist(orderItem1);
            em.persist(orderItem2);
        }

        public List<Delivery> initDelivery(List<Member> members) {
            member1 = members.get(0);
            member2 = members.get(1);

            List<Delivery> deliveryInfos = Arrays.asList(
                    Delivery.createDelivery("서울 금천구 가산디지털1로 189 (주)LG 가산 디지털센터 12층", "010-1111-2222",
                            "직장", member1),
                    Delivery.createDelivery("경기도 화성시", "010-3333-4444", "집",
                            member1),
                    Delivery.createDelivery("경기도 성남시 분당구 판교역로 166 카카오 아지트", "010-1234-5678", "사무실",
                            member2));

            for (Delivery delivery : deliveryInfos) {
                em.persist(delivery);
            }
            return deliveryInfos;
        }

        public List<Funding> initFunding(List<Member> members, List<Item> items) {
            /**
             * member1(임창희 관련) 초기화
             *
             * 장바구니 2개 추가
             * item1, 임창희 -> giftHubItem1
             * item2, 임창희 -> giftHubItem2
             *
             * 펀딩 2개 추가
             * 임창희 -> funding1
             * 임창희 -> funding3
             *
             * 펀딩 아이템 2개 추가
             * item1, funding1 -> fundingItem1
             * item2, funding1 -> fundingItem2
             *
             * item1, funding3 -> fundingItem3
             * item2, funding3 -> fundingItem4
             */
            member1 = members.get(0);
            member2 = members.get(1);
            member3 = members.get(2);
            member4 = members.get(3);

            for (Item item : items) {
                giftHubItem1 = GiftHubItem.createGiftHubItem(1, item, member1);
                giftHubItem2 = GiftHubItem.createGiftHubItem(1, item, member2);
                em.persist(giftHubItem1);
                em.persist(giftHubItem2);
            }

            funding1 = Funding.createFundingForTest(member1, "1번 생일축하해줘", Tag.BIRTHDAY, 10000,
                    LocalDateTime.now().plusDays(14), true);
            funding2 = Funding.createFundingForTest(member2, "2번 생일 축하~",
                    Tag.BIRTHDAY, 100000,
                    LocalDateTime.now().plusDays(14), true);
            em.persist(funding1);
            em.persist(funding2);

            fundingItem1 = FundingItem.createFundingItem(funding1, item1, 1);
            fundingItem2 = FundingItem.createFundingItem(funding1, item2, 2);
            em.persist(fundingItem1);
            em.persist(fundingItem2);
            fundingItem3 = FundingItem.createFundingItem(funding2, item1, 1);
            fundingItem4 = FundingItem.createFundingItem(funding2, item2, 2);
            em.persist(fundingItem3);
            em.persist(fundingItem4);

            /**
             * member2(구태형 관련) 초기화
             *
             * 펀딩 2개 추가
             * 구태형 -> funding2
             * 구태형 -> funding4
             *
             * 펀딩 아이템 2개 추가
             * item1, funding4 -> fundingItem5
             * item2, funding6 -> fundingItem6
             */

            funding3 = Funding.createFunding(member3, "드디어 졸업 성공~~", Tag.GRADUATE,
                    LocalDateTime.now().plusDays(7));
            funding4 = Funding.createFundingForTest(member4, "졸업 성공~~",
                    Tag.GRADUATE, 200000,
                    LocalDateTime.now().plusDays(7), true);
            em.persist(funding3);
            em.persist(funding4);

            FundingItem fundingItem5 = FundingItem.createFundingItem(funding3, item1, 1);
            FundingItem fundingItem6 = FundingItem.createFundingItem(funding3, item2, 2);
            em.persist(fundingItem5);
            em.persist(fundingItem6);

            FundingItem fundingItem7 = FundingItem.createFundingItem(funding4, item1, 1);
            FundingItem fundingItem8 = FundingItem.createFundingItem(funding4, item2, 2);
            em.persist(fundingItem7);
            em.persist(fundingItem8);

            return List.of(funding1, funding2, funding3, funding4);
        }

        public void initRelationships(List<Member> members) {
            member1 = members.get(0);
            member2 = members.get(1);
            member5 = members.get(4);
            member6 = members.get(5);

            List<Relationship> relationshipList = Relationship.createRelationships(member1,
                    member2);

            List<Relationship> relationshipList1 = Relationship.createRelationships(member1, member5);
            List<Relationship> relationshipList2 = Relationship.createRelationships(member1, member6);

            for (Relationship relationship : relationshipList) {
                em.persist(relationship);
            }

            for (Relationship relationship : relationshipList1) {
                em.persist(relationship);
            }

            for (Relationship relationship : relationshipList2) {
                em.persist(relationship);
            }
        }

        public List<Member> initMember() {
            List<Member> memberInfos = Arrays.asList(
                    Member.createMemberWithPoint("임창희", "dlackdgml3710@gmail.com", "",
                            "https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg",
                            46000,
                            "", "aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83"),
                    Member.createMemberWithPoint("구태형", "rnxogud136@gmail.com", "",
                            "https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg",
                            999999999,
                            "", "aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA"),
                    Member.createMemberWithPoint("맹인호", "aoddlsgh98@gmail.com", "",
                            "https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg",
                            200000,
                            "", "aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ"),
                    Member.createMemberWithPoint("양혜인", "helen66626662@gmail.com", "",
                            "https://p.kakaocdn.net/th/talkp/woGALKKcHt/jiOhwZDs9RTkkXPwNYjxF1/wzruf2_110x110_c.jpg",
                            300000,
                            "", "aFtpXm1ZaVtuQnRMeUp9Tn5PY1JiV2JRaF8z"),
                    Member.createMemberWithPoint("이재현", "jhyun030299@gmail.com", "",
                            "https://k.kakaocdn.net/dn/jrT50/btsF9BGMPni/7oxQfq58KmKxIl8UX01mn0/img_110x110.jpg",
                            400000,
                            "",
                            "aFpqUmZVYFRsQHFIfU53R3ZDdlprW25baFFmDw"),
                    Member.createMemberWithPoint("현세미", "gustpal08@gmail.com", "", "",
                            500000,
                            "", "aFlvVm9bbFpoRHBGf0Z0RHRDb15uW25dZFM_")
            );

            for (Member member : memberInfos) {
                em.persist(member);
            }
            return memberInfos;
        }

        public void initContributor(List<Member> members, List<Funding> fundings) {
            member3 = members.get(3);
            member4 = members.get(4);
            member5 = members.get(5);

            funding1 = fundings.get(0);

            Contributor contributor1 = Contributor.createContributor(10000, member3, funding1);
            Contributor contributor2 = Contributor.createContributor(20000, member4, funding1);
            Contributor contributor3 = Contributor.createContributor(20000, member5, funding1);
            em.persist(contributor1);
            em.persist(contributor2);
            em.persist(contributor3);
        }

        public List<Item> initBeauty() {
            List<Item> itemInfos = Arrays.asList(
                    Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                            "샤넬", "뷰티", "00:00"),

                    Item.createItem("NEW 루쥬 코코 밤(+샤넬 기프트 카드)", 51000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg",
                            "샤넬", "뷰티", "934 코랄린 [NEW]"),

                    Item.createItem("코코 마드모아젤 헤어 미스트 35ml", 85000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg",
                            "샤넬", "뷰티", "코코 마드모아젤 헤어 미스트 35ml"),

                    Item.createItem("레 베쥬 립 밤", 85000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220316102858_5380e2c951fc4661a0ec7b08a0bc96ee.jpg",
                            "샤넬", "뷰티", "미디엄"),

                    Item.createItem("[단독+각인+포장] 입생로랑 1위 NEW 벨벳 틴트 세트(+리브르 향수 1.2ml)", 49000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327134440_58d0d7e4b2ea4baebae1b4a3c065198c.jpg",
                            "입생로랑", "뷰티", "220 컨트롤 블러시 (NEW - 로지 코랄)"),

                    Item.createItem("[선물포장] 리브르 핸드 크림 30ml", 33000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240415092310_78cc616347524a47b0cbc3f799a6193b.jpg",
                            "입생로랑", "뷰티", "[선물포장] 리브르 핸드 크림 30ml"),

                    Item.createItem("[각인+포장] NEW 엉크르 드 뽀 쿠션 세트(+라운드 파우치)", 108000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327140332_02835b48b1bc4faeaaaaad51f48aa62b.jpg",
                            "입생로랑", "뷰티", "10호"),

                    Item.createItem("[단독/리미티드선물포장] 코롱 9ML", 32000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411173509_791cf65118c142c68621829f4e2a42df.jpg",
                            "조말론런던", "뷰티", "블랙베리 앤 베이 코롱 9ML"),

                    Item.createItem("[선물포장] 바디 크림 50ML", 45000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240315092736_2ffa91bc2e0d4430bb0fa69db5d2a431.jpg",
                            "조말론런던", "뷰티", "블랙베리 앤 베이 바디 크림 50ML"),

                    Item.createItem("[단독각인/슬리브선물포장] 코롱 30ML", 110000,
                            "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411170225_64f314ccd05a4570a90add7ee46480b4.jpg",
                            "조말론런던", "뷰티", "블랙베리 앤 베이 코롱 30ML"));

            for (Item item : itemInfos) {
                em.persist(item);
            }
            return itemInfos;
        }

        public void initBookmark(List<Member> members, List<Item> items) {

            member1 = members.get(0);
            member2 = members.get(1);

            item1 = items.get(0);
            item2 = items.get(1);
            item3 = items.get(2);
            item4 = items.get(3);
            item5 = items.get(4);

            bookmark1 = Bookmark.createBookmark(member1, item1);
            bookmark2 = Bookmark.createBookmark(member1, item2);
            bookmark3 = Bookmark.createBookmark(member2, item3);
            bookmark4 = Bookmark.createBookmark(member2, item4);
            bookmark5 = Bookmark.createBookmark(member2, item5);

            em.persist(bookmark1);
            em.persist(bookmark2);
            em.persist(bookmark3);
            em.persist(bookmark4);
            em.persist(bookmark5);

        }
    }
}