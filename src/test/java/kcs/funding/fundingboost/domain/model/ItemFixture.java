package kcs.funding.fundingboost.domain.model;

import java.lang.reflect.Field;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Item;

public class ItemFixture {
    public static Item item1() throws NoSuchFieldException, IllegalAccessException {
        Item item = Item.createItem("NEW 루쥬 알뤼르 벨벳 뉘 블랑쉬 리미티드 에디션", 61000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240319133310_1fda0cf74e4f43608184bce3050ae22a.jpg",
                "샤넬", "뷰티", "00:00");
        Field itemId = item.getClass().getDeclaredField("itemId");
        itemId.setAccessible(true);
        itemId.set(item, 1L);

        return item;
    }

    public static Item item2() throws NoSuchFieldException, IllegalAccessException {
        Item item = Item.createItem("NEW 루쥬 코코 밤(+샤넬 기프트 카드)", 51000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220111185052_b92447cb764d470ead70b2d0fe75fe5c.jpg",
                "샤넬", "뷰티", "934 코랄린 [NEW]");
        Field itemId = item.getClass().getDeclaredField("itemId");
        itemId.setAccessible(true);
        itemId.set(item, 2L);
        return item;
    }

    public static Item item3() throws NoSuchFieldException, IllegalAccessException {
        Item item = Item.createItem("코코 마드모아젤 헤어 미스트 35ml", 85000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20230221174618_235ba31681ad4af4806ae974884abb99.jpg",
                "샤넬", "뷰티", "코코 마드모아젤 헤어 미스트 35ml");
        Field itemId = item.getClass().getDeclaredField("itemId");
        itemId.setAccessible(true);
        itemId.set(item, 3L);
        return item;
    }

    public static Item item4() throws NoSuchFieldException, IllegalAccessException {
        Item item = Item.createItem("레 베쥬 립 밤", 85000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20220316102858_5380e2c951fc4661a0ec7b08a0bc96ee.jpg",
                "샤넬", "뷰티", "미디엄");
        Field itemId = item.getClass().getDeclaredField("itemId");
        itemId.setAccessible(true);
        itemId.set(item, 4L);
        return item;
    }

    public static Item item5() throws NoSuchFieldException, IllegalAccessException {
        Item item = Item.createItem("[단독+각인+포장] 입생로랑 1위 NEW 벨벳 틴트 세트(+리브르 향수 1.2ml)", 49000,
                "https://img1.kakaocdn.net/thumb/C320x320@2x.fwebp.q82/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240327134440_58d0d7e4b2ea4baebae1b4a3c065198c.jpg",
                "입생로랑", "뷰티", "220 컨트롤 블러시 (NEW - 로지 코랄)");
        Field itemId = item.getClass().getDeclaredField("itemId");
        itemId.setAccessible(true);
        itemId.set(item, 5L);
        return item;
    }

    public static List<Item> items5() throws NoSuchFieldException, IllegalAccessException {
        return List.of(item1(), item2(), item3(), item4(), item5());
    }

    public static List<Item> items3() throws NoSuchFieldException, IllegalAccessException {
        return List.of(item1(), item2(), item3());
    }
}