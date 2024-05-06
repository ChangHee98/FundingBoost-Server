package kcs.funding.fundingboost.domain.model;

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

    public static FundingItem fundingItem1(Item item, Funding funding) {
        return FundingItem.createFundingItem(funding, item, 1);
    }

    public static FundingItem fundingItem1FinishFunding(Item item, Funding funding) {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 1);
        fundingItem.finishFundingItem();
        return fundingItem;
    }

    public static FundingItem fundingItem2(Item item, Funding funding) {
        return FundingItem.createFundingItem(funding, item, 2);
    }

    public static FundingItem fundingItem2FinishFunding(Item item, Funding funding) {
        FundingItem fundingItem = FundingItem.createFundingItem(funding, item, 2);
        fundingItem.finishFundingItem();
        return fundingItem;
    }

    public static FundingItem fundingItem3(Item item, Funding funding) {
        return FundingItem.createFundingItem(funding, item, 3);
    }
}
