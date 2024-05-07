package kcs.funding.fundingboost.domain.service.utils;

import static kcs.funding.fundingboost.domain.exception.ErrorCode.INVALID_POINT_LACK;

import kcs.funding.fundingboost.domain.entity.Member;
import kcs.funding.fundingboost.domain.exception.CommonException;

public class PayUtils {
    public static void deductPointsIfPossible(Member member, int points) {
        if (points == 0) {
            return;
        }
        if (member.getPoint() - points >= 0) {
            member.minusPoint(points);
        } else {
            throw new CommonException(INVALID_POINT_LACK);
        }
    }
}
