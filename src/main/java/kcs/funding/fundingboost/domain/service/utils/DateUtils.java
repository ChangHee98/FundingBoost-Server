package kcs.funding.fundingboost.domain.service.utils;

import static kcs.funding.fundingboost.domain.service.utils.FundingConst.FUNDING_FINISHED_MESSAGE;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import kcs.funding.fundingboost.domain.entity.Funding;

public class DateUtils {
    public static String toDeadlineString(Funding funding) {
        int fundingStatus = (int) ChronoUnit.SECONDS.between(LocalDateTime.now(), funding.getDeadline());
        int leftDate = (int) ChronoUnit.DAYS.between(LocalDate.now(), funding.getDeadline());
        if (fundingStatus >= 0) {
            return "D-" + leftDate;
        } else {
            return FUNDING_FINISHED_MESSAGE;
        }
    }
}
