CREATE SCHEMA IF NOT EXISTS `fundingboost` DEFAULT CHARACTER SET utf8mb4;

GRANT ALL ON *.* TO 'root'@'localhost' IDENTIFIED BY 'root' WITH GRANT OPTION;
GRANT ALL ON fundingboost.* TO 'root'@'localhost';
FLUSH PRIVILEGES;

USE `fundingboost`;

CREATE TABLE `Member` (
    `member_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `nick_name` VARCHAR(20) NOT NULL,
    `email` VARCHAR(50) NOT NULL    COMMENT '이메일은 == 아이디',
    `password`  VARCHAR(100)    NOT NULL,
    `profile_img_url`   VARCHAR(100)    NOT NULL,
    `point` INT NOT NULL    DEFAULT 0,
    `refresh_token` VARCHAR(255)    NULL,
    `kakao_uuid`    VARCHAR(100)    NOT NULL    COMMENT '카카오 api의 uuid 통해 감사 메시지 전송',
    PRIMARY KEY (`member_id`)
);
CREATE TABLE `Item` (
    `item_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `item_name` VARCHAR(100)    NOT NULL,
    `item_price`    INT NOT NULL,
    `item_image_url`    VARCHAR(255)    NOT NULL,
    `brand_name`    VARCHAR(100)    NOT NULL,
    `category`  VARCHAR(100)  NOT NULL,
    `option_name`   VARCHAR(100)    NULL,
     PRIMARY KEY (`item_id`)
);
CREATE TABLE `Review` (
    `review_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `rating`    INT NOT NULL    COMMENT '1~5',
    `content`   VARCHAR(100)    NOT NULL,
    `member_id` BIGINT  NOT NULL,
    `item_id`   BIGINT  NOT NULL,
    primary key (`review_id`),
    foreign key (`member_id`) references Member(member_id),
    foreign key (`item_id`) references Item(item_id)
);
CREATE TABLE `Delivery` (
    `delivery_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `address`    VARCHAR(200)    NULL,
    `phone_number`  VARCHAR(13) NULL,
    `customer_name` VARCHAR(50) NULL,
    `member_id` BIGINT  NOT NULL,
    primary key (`delivery_id`),
    foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `GiftHub_Item` (
    `giftHub_Item_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `quantity`  INT NOT NULL    DEFAULT 1,
    `item_id`   BIGINT  NOT NULL,
    `member_id` BIGINT  NOT NULL,
    primary key (`giftHub_Item_id`),
    foreign key (`item_id`) references Item(item_id),
    foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `Orders` (
    `order_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `total_price`   INT NOT NULL    COMMENT '주문 총가격',
    `member_id` BIGINT  NOT NULL,
    `delivery_id`   BIGINT  NOT NULL,
    primary key (`order_id`),
    foreign key (`delivery_id`) references Delivery(delivery_id),
    foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `Bookmark` (
    `favorite_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `member_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `item_id`   BIGINT  NOT NULL,
     primary key (`favorite_id`),
     foreign key (`item_id`) references Item(item_id),
     foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `Funding` (
    `funding_id`    BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `message`   VARCHAR(50) NULL    COMMENT '최대 20자이내로 입력 가능',
    `tag`   VARCHAR(10) NULL    COMMENT 'Enum (#생일, #졸업, #기타)',
    `total_price`   INT NOT NULL    COMMENT '펀딩으로 모아야 할  총 금액',
    `collect_price` INT NOT NULL    DEFAULT 0   COMMENT '펀딩으로 현재 모인 금액',
    `deadline`  DATE    NOT NULL    COMMENT 'LocalDateTime.now()와 deadline이 일치하면 펀딩종료',
    `funding_status` BOOLEAN NOT NULL    COMMENT '0 : 진행 종료, 1 : 진행중',
    `member_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
     primary key (`funding_id`),
     foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `Relationship` (
    `relation_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `member_id` BIGINT  NOT NULL,
    `friend_id` BIGINT  NOT NULL,
     primary key (`relation_id`),
     foreign key (`friend_id`) references Member(member_id),
     foreign key (`member_id`) references Member(member_id)
);
CREATE TABLE `Funding_Item` (
    `funding_item_id`   BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `funding_id`    BIGINT  NOT NULL,
    `item_id`   BIGINT  NOT NULL,
    `funding_item_sequence` INT NULL,
    `item_status`   BOOLEAN NOT NULL    COMMENT '0 : 펀딩 아이템 완료 1 : 펀딩 아이템 완료되지 않음',
    `finished_status`   BOOLEAN NOT NULL    COMMENT '0 : 배송지 입력하거나 포인트 전환한 경우 1 : 배송지  안하거나 포인트 전환 안한 경우',
     primary key (`funding_item_id`),
     foreign key (`funding_id`) references Funding(funding_id),
     foreign key (`item_id`) references Item(item_id)
);
CREATE TABLE `Contributor` (
    `contributor_id`    BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `contributor_price` INT NOT NULL    COMMENT '사용자 펀딩금액(최대: 총 펀딩 금액보다 크면 안됨)',
    `member_id` BIGINT  NOT NULL    COMMENT '`내`가 올린 펀딩에 대해 펀딩한 친구들 PK',
    `funding_id`    BIGINT  NOT NULL,
     primary key (`contributor_id`),
     foreign key (`member_id`) references Member(member_id),
     foreign key (`funding_id`) references Funding(funding_id)
);
CREATE TABLE `Order_Item` (
    `order_item_id` BIGINT  NOT NULL    COMMENT 'Auto Increament',
    `quantity`  INT NOT NULL    DEFAULT 1   COMMENT '한 종류의 상품 당 수량',
    `item_id`   BIGINT  NOT NULL,
    `order_id` BIGINT  NOT NULL,
     primary key (`order_item_id`),
     foreign key (`item_id`) references Item(item_id),
     foreign key (`order_id`) references Orders(order_id)
);


insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.939+0900','dlackdgml3710@gmail.com','aFxoWGFUZlV5SH9MfE9-TH1PY1JiV2JRaF83','2024-04-19T11:28:17.939+0900','임창희','',46000,'https://p.kakaocdn.net/th/talkp/wnbbRhlyRW/XaGAXxS1OkUtXnomt6S4IK/ky0f9a_110x110_c.jpg','',1);
insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.966+0900','rnxogud136@gmail.com','aFtpX2lZaFhvQ3JLe0J2QnFDcFxtXWhdbldgDA','2024-04-19T11:28:17.966+0900','구태형','',999999999,'https://p.kakaocdn.net/th/talkp/wowkAlwbLn/Ko25X6eV5bs1OycAz7n9Q1/lq4mv6_110x110_c.jpg','',2);
insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.967+0900','aoddlsgh98@gmail.com','aFluW29Ya1hpRXdBdEdyQHBGdlprW25baFFmDQ','2024-04-19T11:28:17.967+0900','맹인호','',200000,'https://p.kakaocdn.net/th/talkp/woBG0lIJfU/M6aVERkQ2Lv2sNfQaLMYrK/pzfmfl_110x110_c.jpg','',3);
insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.968+0900','helen66626662@gmail.com','aFtpXm1ZaVtuQnRMeUp9Tn5PY1JiV2JRaF8z','2024-04-19T11:28:17.968+0900','양혜인','',300000,'https://p.kakaocdn.net/th/talkp/woGALKKcHt/jiOhwZDs9RTkkXPwNYjxF1/wzruf2_110x110_c.jpg','',4);
insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.968+0900','jhyun030299@gmail.com','aFpqUmZVYFRsQHFIfU53R3ZDdlprW25baFFmDw','2024-04-19T11:28:17.968+0900','이재현','',400000,'https://k.kakaocdn.net/dn/jrT50/btsF9BGMPni/7oxQfq58KmKxIl8UX01mn0/img_110x110.jpg','',5);
insert into MEMBER (created_date,email,kakao_uuid,modified_date,nick_name,password,point,profile_img_url,refresh_token,member_id) values ('2024-04-19T11:28:17.969+0900','gustpal08@gmail.com','aFlvVm9bbFpoRHBGf0Z0RHRDb15uW25dZFM_','2024-04-19T11:28:17.969+0900','현세미','',500000,'','',6);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('샤넬','뷰티','2024-04-19T11:28:17.979+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg','NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션',61000,'2024-04-19T11:28:17.979+0900','00:00',1);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('샤넬','뷰티','2024-04-19T11:28:17.981+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg','NEW 루쥬 코코 밤(+샤넬 기프트 카드)',51000,'2024-04-19T11:28:17.981+0900','934 코랄린 [NEW]',2);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('샤넬','뷰티','2024-04-19T11:28:17.982+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg','코코 마드모아젤 헤어 미스트 35ml',85000,'2024-04-19T11:28:17.982+0900','코코 마드모아젤 헤어 미스트 35ml',3);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('샤넬','뷰티','2024-04-19T11:28:17.982+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220316102858_5380e2c951fc4661a0ec7b08a0bc96ee.jpg','레 베쥬 립 밤',85000,'2024-04-19T11:28:17.982+0900','미디엄',4);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('입생로랑','뷰티','2024-04-19T11:28:17.983+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327134440_58d0d7e4b2ea4baebae1b4a3c065198c.jpg','[단독+각인+포장] 입생로랑 1위 NEW 벨벳 틴트 세트(+리브르 향수 1.2ml)',49000,'2024-04-19T11:28:17.983+0900','220 컨트롤 블러시 (NEW - 로지 코랄)',5);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('입생로랑','뷰티','2024-04-19T11:28:17.983+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240415092310_78cc616347524a47b0cbc3f799a6193b.jpg','[선물포장] 리브르 핸드 크림 30ml',33000,'2024-04-19T11:28:17.983+0900','[선물포장] 리브르 핸드 크림 30ml',6);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('입생로랑','뷰티','2024-04-19T11:28:17.986+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327140332_02835b48b1bc4faeaaaaad51f48aa62b.jpg','[각인+포장] NEW 엉크르 드 뽀 쿠션 세트(+라운드 파우치)',108000,'2024-04-19T11:28:17.986+0900','10호',7);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('조말론런던','뷰티','2024-04-19T11:28:17.987+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411173509_791cf65118c142c68621829f4e2a42df.jpg','[단독/리미티드선물포장] 코롱 9ML',32000,'2024-04-19T11:28:17.987+0900','블랙베리 앤 베이 코롱 9ML',8);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('조말론런던','뷰티','2024-04-19T11:28:17.988+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240315092736_2ffa91bc2e0d4430bb0fa69db5d2a431.jpg','[선물포장] 바디 크림 50ML',45000,'2024-04-19T11:28:17.988+0900','블랙베리 앤 베이 바디 크림 50ML',9);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('조말론런던','뷰티','2024-04-19T11:28:17.988+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240411170225_64f314ccd05a4570a90add7ee46480b4.jpg','[단독각인/슬리브선물포장] 코롱 30ML',110000,'2024-04-19T11:28:17.988+0900','블랙베리 앤 베이 코롱 30ML',10);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('티파니앤코', '패션','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20201113163447_fa637a9163a8446db21c029e41fe0c4b.jpg','티파니 T1 와이드 힌지드 뱅글', 11100000,'2024-04-19T11:28:17.979+0900', '티파니 T1 와이드 힌지드 뱅글(미디움)', 11);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('티파니앤코', '패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230607184420_2be0f56fd7e64cbf873bc195ed8b7a78.jpg','티파니 T1 스몰 써클 펜던트 (18K 로즈골드)', 5500000, '2024-04-19T11:28:17.979+0900', '티파니 T1 스몰 써클 펜던트 (18K 로즈골드)', 12);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('티파니앤코', '패션','2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240329163232_e0b5b7650718467b9dd34752f814aba9.jpg', '티파니 T 스마일 펜던트 (스몰, 18K 옐로우 골드)', 1700000, '2024-04-19T11:28:17.979+0900', '티파니 T 스마일 펜던트 (스몰)', 13);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('몽블랑', '패션','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131150315_dc352197be5f4225b76422834e5eba9b.jpg', '[단독] 사토리얼 5cc 카드 지갑 더스티 블루 198245',  320000, '2024-04-19T11:28:17.979+0900', '[단독] 사토리얼 5cc 카드 지갑 더스티 블루 198245',14);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('몽블랑', '패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240227151217_c5cde202c68c40c9bdf452f69634c2a1.jpg', '사토리얼 4cc 카드/명함 지갑 클레이 198241', 410000, '2024-04-19T11:28:17.979+0900', '사토리얼 4cc 카드/명함 지갑 클레이 198241',15);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('몽블랑','패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231124162609_ec8347e3c9d948cbad54edfeb6f313de.png', '픽스 블랙 볼펜 132495',  410000, '2024-04-19T11:28:17.979+0900', '픽스 블랙 볼펜 132495',16);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('몽블랑', '패션','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230619153421_e3ebb9c1194a42d4bf6d74e64954a91f.jpg', '사토리얼 5cc 카드 지갑 130324', 32000, '2024-04-19T11:28:17.979+0900', '사토리얼 5cc 카드 지갑 130324',17);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('구찌', '패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240314092155_6690cfb440e04398ba371793ecfafa61', '[GG 마몽] 수퍼 미니백', 1890000, '2024-04-19T11:28:17.979+0900', '[GG 마몽] 수퍼 미니백',18);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('구찌', '패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231114083558_e09df89addd1493ebd7aca3c1ffa9409.jpg', '구찌 스크립트 카드 케이스',420000,'2024-04-19T11:28:17.979+0900', '구찌 스크립트 카드 케이스',19);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('구찌', '패션', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231111050644_41e515e818a94bd0b08a777cc24e7533.jpg', '구찌 스크립트 미니 지갑', 900000, '2024-04-19T11:28:17.979+0900', '구찌 스크립트 미니 지갑',20);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('농협안심한우', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231227090524_1b979e0b92184f37a79b5909c4fb2298.png','농협안심한우 1등급 소한마리알찬세트 1.35kg (등심250g+채끝200g+장조림300g+불고기300g+국거리300g) 원산지 : 국내산', 94800, '2024-04-19T11:28:17.979+0900', '농협안심한우 1등급 소한마리알찬세트 1.35kg (등심250g+채끝200g+장조림300g+불고기300g+국거리300g)', 21);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('현대쌀집', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230830142538_2aab21d6940a44b1bdd0a38515e535d9.jpg', '비단같은 윤기 비단쌀 2kg 원산지 : 국내산', 15700, '2024-04-19T11:28:17.979+0900', '비단같은 윤기 비단쌀 2kg', 22);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ( '스타벅스', '식품', '2024-04-19T11:28:17.979+0900','https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240315134009_48b4dcb12c0a4e1f996cdadee4f60135.jpg', '스타벅스 클래식 넛츠 타르트', 30000, '2024-04-19T11:28:17.979+0900', '스타벅스 클래식 넛츠 타르트', 23);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('경복궁', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240109175838_70da4296f30f466d8058d18cdb14e1aa.jpg',  '[경복궁] 보리굴비 실속세트(보리굴비 290g*4미+보성말차 1box)', 110000, '2024-04-19T11:28:17.979+0900','[경복궁] 보리굴비 실속세트(보리굴비 290g*4미+보성말차 1box)', 24);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('본죽식품', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230329110522_d0c199c7b1d3458ea11bdfc6afca4829.jpg',  '아프다고 굶지마요, 본죽 14종+장조림 set (총 7팩/10팩)', 19900, '2024-04-19T11:28:17.979+0900', ' 02_★BEST★200g전복/쇠고기/낙지김치/보양삼계/단호박+미니 장조림 5팩 ', 25);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('이야이야앤프랜즈', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230817175251_44a5ff64afde43b1a7f900eedb9141f7.jpg','최고급 그리스 엑스트라 버진 올리브오일 3종 선물세트', 123000, '2024-04-19T11:28:17.979+0900',  '최고급 그리스 엑스트라 버진 올리브오일 3종 선물세트', 26);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ( '워커힐호텔', '식품','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220217100152_9854e082dde34b0baadded69bfabc4f2.jpg','[워커힐호텔]명월관 명품 갈비탕 6팩x600g', 102000, '2024-04-19T11:28:17.979+0900', '[워커힐호텔]명월관 명품 갈비탕 6팩x600g', 27);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('온브릭스', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240223161135_1fa6ffa4fde74bb786eefd39964b989d.jpg', '[보자기증정] 고급 과일바구니, 카멜 2호 10종 6.8kg이상(멜론,애플망고)', 139800,  '2024-04-19T11:28:17.979+0900', '[보자기증정] 고급 과일바구니, 카멜 2호 10종 6.8kg이상(멜론,애플망고)', 28);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('TWG Tea(LuX)', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231003221734_aaeb9a411e074a949ea76e036fefd658.jpg', 'TWG Tea BON VOYAGE 티백 디카페인 3종세트 (3개 골라담기)', 111000,  '2024-04-19T11:28:17.979+0900', '미드나이트 아워 티', 29);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('델리스', '식품', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213153404_b9db7167008942c590f09d5bf9a8a91f.jpg', '와인 치즈 플래터 11종(무알콜와인+치즈+샤퀴테리+크래커+올리브+캐비어) 스파클링와인 와인안주 생일 결혼기념일 집들이 혼술', 199800, '2024-04-19T11:28:17.979+0900',  '와인 치즈 플래터 11종(무알콜와인+치즈+샤퀴테리+크래커+올리브+캐비어) 스파클링와인 와인안주 생일 결혼기념일 집들이 혼술', 30);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('메디큐브 에이지알', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240408205352_b011db2c5cd642faaadb3f62b806c59d.jpg', '[카카오 단독] [김희선 PICK] 1등기기 부스터 프로 (파우치&리프팅크림 증정)', 339000, '2024-04-19T11:28:17.979+0900',  '본품', 31);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('Apple', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231012202825_9cdbe5b636ae4f2e9dfcec7ef6af1bc4.jpg', 'Apple 에어팟 프로 2세대 USB-C 타입 (MTJV3KH/A)', 51000, '2024-04-19T11:28:17.979+0900', '에어팟 프로 2세대(C타입) MTJV3KH/A', 32);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('Apple', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230920133803_e7c2b1be1e1942a88d503016d0222b4e.jpg',  'Apple 아이폰 15 128GB 자급제', 1250000,  '2024-04-19T11:28:17.979+0900', '블랙', 33);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('오드', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FOPwECGRDycdUbcRBDOeH1g%2FVH7v13Fcmw9mgjyw095DWFt1sT2xxT6pW_p6e-AxhUk.jpg', '[현대백화점] 라이라복스 칼로타 하이엔드 액티브 스피커 Lyravox Karlotta', 43900000, '2024-04-19T11:28:17.979+0900', '라이라복스 칼로타 하이엔드 액티브 스피커 Lyravox Karlotta', 34);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('삼성전자', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230315111431_36fb8b3bf1b7478ea1cf1c36b5654ddc.jpg', '삼성 BESPOKE 무풍에어컨 갤러리 청정 홈멀티 홈멀티/ 기본설치비무료 / 삼성물류직송', 6621600, '2024-04-19T11:28:17.979+0900', '일반배관', 35);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('쿠쿠', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240202144335_eef562abafe645839fc8316a191bef79.jpg', '쿠쿠 리네이처 컴팩트 안마의자 CMS-G210NW', 3590000, '2024-04-19T11:28:17.979+0900', '쿠쿠 리네이처 컴팩트 안마의자 CMS-G210NW', 36);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('LG전자', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230620153643_326272ab17a94ed8bed41ae13280cbe2.jpg', 'LG디오스 식기세척기+전기레인지 DUBJ2GAL+BEI3GQUO (DUBJ2GAL + BEI3GQUO)', 3000000, '2024-04-19T11:28:17.979+0900', 'LG디오스 식기세척기+전기레인지 DUBJ2GAL+BEI3GQUO', 37);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('다이슨(일반)', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FSFSDxyVdWVoJ4lb06M2p-Q%2FluWBima0QGAHou-f3EwL8kbFwbP3QTtbIIdjdGiqgUI.jpg', '다이슨 무선청소기 V15 디텍트(골드/골드)', 1290000, '2024-04-19T11:28:17.979+0900', '다이슨 무선청소기 V15 디텍트(골드/골드)', 38);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('아이닉', '디지털', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230323142713_72b6769f880d47e29de5db2ada395fe9.jpg', '[당일발송] 가전필수템 아이닉 올인원 로봇청소기 iX10', 899000, '2024-04-19T11:28:17.979+0900', 'iX10 로봇청소기 화이트', 39);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('소니플레이스테이션', '디지털', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231219095942_942598d5ce424fef96953ffafee2cb63.jpg', '[PS5] PlayStation5 슬림 디지털 에디션 (플레이스테이션 5 825GB)', 558000, '2024-04-19T11:28:17.979+0900', '[PS5] PlayStation5 슬림 디지털 에디션', 40);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('스타벅스', '리빙/도서','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231227152358_f775b058619c4c4fa26d0f535f9b8468.jpg','선물하기 좋아요 그린 or 크림 스탠리 켄처 텀블러 +에코텀블러음료쿠폰(MMS발송)', 130000,  '2024-04-19T11:28:17.979+0900',  'SS 스탠리 그린 컨처 텀블러 591ml', 41);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('렉슨', '리빙/도서', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20231110133124_488019c472804206a0a044e52af2606a.jpg',  'STELI 스텔리 무드등 다이닝 침실 스탠드 조명 - LH95', 114000,'2024-04-19T11:28:17.979+0900',  '골드(DOME) - LH95D-D',42);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('까사미아', '리빙/도서', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FLnldKldJtt3gFKjSd4jGPg%2FE2DlYb6apHJJJ0uwBa26J1106e9aUwKVGhiNEqtVt9o.jpg', '우스터 1인 전동 리클라이너_브라운', 1590000,  '2024-04-19T11:28:17.979+0900',  '브라운',43);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('에이스침대', '리빙/도서', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FDCRNEgQye1j6Xm6ht9v_lA%2FJ_PfBm-1icT6RjHMIyxLDFZCOHUtAf85V_z6-56owdQ.jpg', '[에이스침대]BRA 1441-E AT등급/SS(슈퍼싱글사이즈)', 1592000, '2024-04-19T11:28:17.979+0900',  '라이트그레이',44);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ( '인터데코', '리빙/도서','2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FwlbUrJzbiq_LAha_qm-iBQ%2FMQMwppXn3E13sA4xCVMEo_TPOmTY5U0PiO2mC-sRxFg.jpg', '아벨리아 거실장세트4(단문장+거실장1500+협탁)', 1547000, '2024-04-19T11:28:17.979+0900',  '',45);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('노먼', '리빙/도서', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210930150604_d308a97c287147b0a2955ea422189bc9.jpg',  '[NOMON] DARO 노몬 다로', 1525100, '2024-04-19T11:28:17.979+0900', '',46);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('악기',  '뷰티리빙/도서', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230711093002_594eaada3ba44b1cabae96d8540ca9e6.jpg', 'Artesia Pro 아르테시아 전자드럼 올메쉬 A50 SET2 풀패키지', 1510000,  '2024-04-19T11:28:17.979+0900', '',47);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('봄소와', '리빙/도서',  '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F3Wbt8mb8Xl32vd40X5sXMA%2F6_aTEbpQadLhvl8F-3lW0VaM47yFyZtzhHeMnIuSgys.jpg', '차나 3인용 아쿠아클린 패브릭 소파', 1480000, '2024-04-19T11:28:17.979+0900',  'Alhambra 1', 48);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('클라르하임', '리빙/도서', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220708094538_380dbfd23e6f4df8ab87c0fdfea2d732.JPG',   '[갤러리아] 아뜨리에 100 폴란드 프리미엄 구스다운 이불솜 K', 1478520, '2024-04-19T11:28:17.979+0900',  '화이트', 49);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('빅토리녹스', '리빙/도서',  '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2Fy29ROOeKyyftcs8NxEgtAw%2FC4jsd78xw7J6dQhmfBe_tK_7A8yyUIzLR0ambN36Ehc','[롯데백화점][빅토리녹스 공식] 이녹스 크로노 블루 다이얼 실버 브레이슬릿 시계 241985', 1436500, '2024-04-19T11:28:17.979+0900',  '블루 다이얼 실버 브레이슬릿', 50);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('아디다스 피트니스', '스포츠', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230511154115_da57987fafee42aaac243d397e57afc0.jpg', '아디다스 런닝머신 T-19i 가정용 유산소 접이식 아파트 워킹 저소음 패드 실내 트레드밀', 2580000, '2024-04-19T11:28:17.979+0900', '아디다스 런닝머신 T-19i 가정용 유산소 접이식 아파트 워킹 저소음 패드 실내 트레드밀', 51);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('골프용품', '스포츠',  '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2FVXUgMTnWP3QnWT-Q7UWTZA%2F2IAf-Cnk5iv0e8SQP9LHCvSV_QMNbjripQrrFB6JpYU.jpg', '프로기어 PRGR 2024 슈퍼에그 카본 여성 7아이언세트 GC', 2520000, '2024-04-19T11:28:17.979+0900', '여성7아이언',52 );
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('코베아', '스포츠', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210430124143_09107a814d6a4bfaa9803e1113cf9548.png', '코베아 아웃백 시그니처', 1590000, '2024-04-19T11:28:17.979+0900', '코베아 아웃백 시그니처', 53);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('AU테크', '스포츠', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230419111854_aabe46a7905f49e0a9420f28b340c69f.jpg', 'AU테크 스카닉 2X 배달용 고출력 전기자전거 48V 15Ah', 879000, '2024-04-19T11:28:17.979+0900', '블랙 {PH}', 54);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('메르세데스-벤츠', '스포츠', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230508173419_0af838189c854da5b2ef88d56a14cab0.png', '여성 손목 시계', 553300, '2024-04-19T11:28:17.979+0900', '여성 손목 시계', 55);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('반석스포츠', '스포츠', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230502093549_b90c730f0dd648beaa6dc1cccfa79a88.jpg', '그랜드 전동거꾸리 물구나무서기 스트레칭 기구', 568000,  '2024-04-19T11:28:17.979+0900', '그랜드 전동거꾸리 물구나무서기 스트레칭 기구', 56);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('나노휠', '스포츠', '2024-04-19T11:28:17.979+0900',  'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20221130113512_e49157b0d35b43cca46afc099df0d344.jpg', '[나노휠] 전동킥보드 NQ-01 Plus+ 프리미엄 36V (10.4Ah)', 486300, '2024-04-19T11:28:17.979+0900', '[나노휠] 전동킥보드 NQ-01 Plus+ 프리미엄 36V (10.4Ah)', 57);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('타이틀리스트', '스포츠', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230203142810_9ce2c3a85e2745618fc9ba46d81203b7.jpg', '[골프공 선물 추천] 타이틀리스트 PRO V1 / PRO V1X 골프공', 70000,  '2024-04-19T11:28:17.979+0900', 'PRO_V1', 58);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('아디다스코리아', '스포츠', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2Fvw_r47QTKC24nRRgdC6woQ%2FBqOXmZ4Su512SqCWL72DvmCNE4YV_EfBlFd5lWD8rWQ.jpg', '[단독][BEST/한정수량] 아디다스 미니 에어라이너 백 IL9610', 65000,  '2024-04-19T11:28:17.979+0900', 'NS', 59);
insert into ITEM (brand_name,category,created_date,item_image_url,item_name,item_price,modified_date,option_name,item_id) values ('오센트', '스포츠', '2024-04-19T11:28:17.979+0900', 'https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20210929082549_98e6e54cce164da5bc9e7e588fc2c232.png',  '오센트 스마일리 제주 차량용방향제', 52000,  '2024-04-19T11:28:17.979+0900', '오센트 스마일리 제주 차량용방향제', 60);
insert into DELIVERY (address,created_date,customer_name,delivery_status,member_id,modified_date,phone_number,delivery_id) values ('서울 금천구 가산디지털1로 189 (주)LG 가산 디지털센터 12층','2024-04-19T11:28:17.992+0900','직장',false,1,'2024-04-19T11:28:17.992+0900','010-1111-2222',1);
insert into DELIVERY (address,created_date,customer_name,delivery_status,member_id,modified_date,phone_number,delivery_id) values ('경기도 화성시','2024-04-19T11:28:17.997+0900','집',false,1,'2024-04-19T11:28:17.997+0900','010-3333-4444',2);
insert into DELIVERY (address,created_date,customer_name,delivery_status,member_id,modified_date,phone_number,delivery_id) values ('경기도 성남시 분당구 판교역로 166 카카오 아지트','2024-04-19T11:28:17.998+0900','사무실',false,2,'2024-04-19T11:28:17.998+0900','010-1234-5678',3);
insert into ORDERS (delivery_id,item_id,memeber_id,price,quantity,order_id) values (1,1,1,10700,1,1);
insert into ORDERS (delivery_id,item_id,memeber_id,price,quantity,order_id) values (2,2,1,17820,1,2);
insert into GIFT_HUB_ITEM (created_date,item_id,member_id,modified_date,quantity,gift_hub_item_id) values ('2024-04-19T11:28:18.003+0900',1,1,'2024-04-19T11:28:18.003+0900',1,1);
insert into GIFT_HUB_ITEM (created_date,item_id,member_id,modified_date,quantity,gift_hub_item_id) values ('2024-04-19T11:28:18.004+0900',2,1,'2024-04-19T11:28:18.004+0900',1,2);
insert into FUNDING (collect_price,created_date,deadline,funding_status,member_id,message,modified_date,tag,total_price,funding_id) values (0,'2024-04-19T11:28:18.008+0900','2024-05-03T11:28:18.004+0900',true,1,'생일축하해줘','2024-04-19T11:28:18.008+0900','BIRTHDAY',100000,1);
insert into FUNDING (collect_price,created_date,deadline,funding_status,member_id,message,modified_date,tag,total_price,funding_id) values (10000,'2024-04-19T11:28:18.014+0900','2024-05-03T11:28:18.004+0900',false,1,'생일 축하~','2024-04-19T11:28:18.014+0900','BIRTHDAY',100000,2);
insert into FUNDING (collect_price,created_date,deadline,funding_status,member_id,message,modified_date,tag,total_price,funding_id) values (0,'2024-04-19T11:28:18.018+0900','2024-04-26T11:28:18.018+0900',true,2,'드디어 졸업 성공~~','2024-04-19T11:28:18.018+0900','GRADUATE',200000,3);
insert into FUNDING (collect_price,created_date,deadline,funding_status,member_id,message,modified_date,tag,total_price,funding_id) values (110000,'2024-04-19T11:28:18.019+0900','2024-04-26T11:28:18.018+0900',false,2,'드디어 졸업 성공~~','2024-04-19T11:28:18.019+0900','GRADUATE',200000,4);
insert into FUNDING (collect_price,created_date,deadline,funding_status,member_id,message,modified_date,tag,total_price,funding_id) values (10000,'2024-04-19T11:28:18.028+0900','2024-05-03T11:28:18.028+0900',false,1,'줘','2024-04-19T11:28:18.028+0900','BIRTHDAY',112000,5);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.015+0900',false,1,1,1,false,'2024-04-19T11:28:18.015+0900',1);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.016+0900',false,1,2,2,false,'2024-04-19T11:28:18.016+0900',2);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.017+0900',false,2,1,1,false,'2024-04-19T11:28:18.017+0900',3);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.017+0900',false,2,2,2,false,'2024-04-19T11:28:18.017+0900',4);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.020+0900',false,4,1,1,false,'2024-04-19T11:28:18.020+0900',5);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.020+0900',false,4,2,2,false,'2024-04-19T11:28:18.020+0900',6);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.029+0900',false,5,1,1,false,'2024-04-19T11:28:18.029+0900',7);
insert into FUNDING_ITEM (created_date,delivery_status,funding_id,item_id,item_sequence,item_status,modified_date,funding_item_id) values ('2024-04-19T11:28:18.029+0900',false,5,2,2,false,'2024-04-19T11:28:18.029+0900',8);
insert into RELATIONSHIP (created_date,friend_id,member_id,modified_date,relation_id) values ('2024-04-19T11:28:18.025+0900',2,1,'2024-04-19T11:28:18.025+0900',1);
insert into RELATIONSHIP (created_date,friend_id,member_id,modified_date,relation_id) values ('2024-04-19T11:28:18.027+0900',1,2,'2024-04-19T11:28:18.027+0900',2);
insert into CONTRIBUTOR (contributor_price,created_date,funding_id,member_id,modified_date,contributor_id) values (10000,'2024-04-19T11:28:18.031+0900',5,5,'2024-04-19T11:28:18.031+0900',1);
insert into CONTRIBUTOR (contributor_price,created_date,funding_id,member_id,modified_date,contributor_id) values (20000,'2024-04-19T11:28:18.033+0900',5,6,'2024-04-19T11:28:18.033+0900',2);

-- DROP TABLE IF EXISTS `sample_data`;
-- CREATE TABLE `sample_data` (
--     `id` int(11) NOT NULL AUTO_INCREMENT,
--     `detail` varchar(100) NOT NULL,
--     PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
--
-- INSERT INTO sample_data (`id`,`detail`) VALUES ('1', 'Hello DKOS!');
