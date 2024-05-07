package kcs.funding.fundingboost.domain.service.utils;

import java.util.List;
import kcs.funding.fundingboost.domain.entity.Funding;
import kcs.funding.fundingboost.domain.entity.FundingItem;

public class FundingUtils {
    public static void checkFundingFinished(Funding funding) {

        List<FundingItem> fundingItems = funding.getFundingItems();
        for (FundingItem fundingItem : fundingItems) {
            if (fundingItem.isFinishedStatus()) {
                return;
            }
        }
        funding.finish();
    }
}
