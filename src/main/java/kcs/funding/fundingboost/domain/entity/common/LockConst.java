package kcs.funding.fundingboost.domain.entity.common;

public interface LockConst {
    long LOCK_EXIST_VALIDITY_IN_MILLISECONDS = 864000;
    long ACCESS_TOKEN_VALIDITY_IN_SECONDS = LOCK_EXIST_VALIDITY_IN_MILLISECONDS / 1000;
}
