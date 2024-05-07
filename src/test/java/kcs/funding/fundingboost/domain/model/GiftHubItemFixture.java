package kcs.funding.fundingboost.domain.model;

import java.lang.reflect.Field;
import kcs.funding.fundingboost.domain.entity.GiftHubItem;
import kcs.funding.fundingboost.domain.entity.Item;
import kcs.funding.fundingboost.domain.entity.Member;

public class GiftHubItemFixture {
    public static GiftHubItem quantity1(Item item, Member member) throws NoSuchFieldException, IllegalAccessException {
        GiftHubItem giftHubItem = GiftHubItem.createGiftHubItem(1, item, member);

        Field giftHunItemId = giftHubItem.getClass().getDeclaredField("giftHubItemId");
        giftHunItemId.setAccessible(true);
        giftHunItemId.set(giftHubItem, 1L);

        return giftHubItem;
    }

    public static GiftHubItem quantity2(Item item, Member member) throws NoSuchFieldException, IllegalAccessException {
        GiftHubItem giftHubItem = GiftHubItem.createGiftHubItem(2, item, member);

        Field giftHunItemId = giftHubItem.getClass().getDeclaredField("giftHubItemId");
        giftHunItemId.setAccessible(true);
        giftHunItemId.set(giftHubItem, 2L);

        return giftHubItem;
    }

    public static GiftHubItem quantity3(Item item, Member member) throws NoSuchFieldException, IllegalAccessException {
        GiftHubItem giftHubItem = GiftHubItem.createGiftHubItem(3, item, member);

        Field giftHunItemId = giftHubItem.getClass().getDeclaredField("giftHubItemId");
        giftHunItemId.setAccessible(true);
        giftHunItemId.set(giftHubItem, 3L);

        return giftHubItem;
    }

    public static GiftHubItem quantity4(Item item, Member member) throws NoSuchFieldException, IllegalAccessException {
        GiftHubItem giftHubItem = GiftHubItem.createGiftHubItem(4, item, member);

        Field giftHunItemId = giftHubItem.getClass().getDeclaredField("giftHubItemId");
        giftHunItemId.setAccessible(true);
        giftHunItemId.set(giftHubItem, 4L);

        return giftHubItem;
    }
}
