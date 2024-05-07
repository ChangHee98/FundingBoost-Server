package kcs.funding.fundingboost.domain.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;
import kcs.funding.fundingboost.domain.entity.Item;

public class FundingItemFixture {
    public static List<FundingItem> fundingItems(List<Item> items, Funding funding) {
        ArrayList<FundingItem> fundingItems = new ArrayList<>();
        int i = 1;
        for (Item item : items) {
            fundingItems.add(FundingItem.createFundingItem(funding, item, i++));
        }
        return fundingItems;
    }

    public static FundingItem fundingItem1(Item item, Funding funding)
            throws NoSuchFieldException, IllegalAccessException {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 1);

        Field fundingItemId = fundingItem.getClass().getDeclaredField("fundingItemId");
        fundingItemId.setAccessible(true);
        fundingItemId.set(fundingItem, 1L);

        return fundingItem;
    }

    public static FundingItem fundingItem1FinishFunding(Item item, Funding funding)
            throws IllegalAccessException, NoSuchFieldException {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 1);
        fundingItem.finishFundingItem();

        Field fundingItemId = fundingItem.getClass().getDeclaredField("fundingItemId");
        fundingItemId.setAccessible(true);
        fundingItemId.set(fundingItem, 1L);

        return fundingItem;
    }

    public static FundingItem fundingItem2(Item item, Funding funding)
            throws NoSuchFieldException, IllegalAccessException {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 2);

        Field fundingItemId = fundingItem.getClass().getDeclaredField("fundingItemId");
        fundingItemId.setAccessible(true);
        fundingItemId.set(fundingItem, 2L);

        return fundingItem;
    }

    public static FundingItem fundingItem2FinishFunding(Item item, Funding funding)
            throws IllegalAccessException, NoSuchFieldException {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 2);

        Field fundingItemId = fundingItem.getClass().getDeclaredField("fundingItemId");
        fundingItemId.setAccessible(true);
        fundingItemId.set(fundingItem, 2L);

        return fundingItem;
    }

    public static FundingItem fundingItem3(Item item, Funding funding)
            throws NoSuchFieldException, IllegalAccessException {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 3);

        Field fundingItemId = fundingItem.getClass().getDeclaredField("fundingItemId");
        fundingItemId.setAccessible(true);
        fundingItemId.set(fundingItem, 3L);

        return fundingItem;
    }
}
