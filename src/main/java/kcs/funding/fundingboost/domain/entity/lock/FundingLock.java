package kcs.funding.fundingboost.domain.entity.lock;

import static kcs.funding.fundingboost.domain.entity.common.LockConst.LOCK_EXIST_VALIDITY_IN_MILLISECONDS;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "lock", timeToLive = LOCK_EXIST_VALIDITY_IN_MILLISECONDS)
public class FundingLock {

    @Id
    Long fundingId;

    public static FundingLock createFundingLock(Long fundingId) {
        FundingLock fundingLock = new FundingLock();
        fundingLock.fundingId = fundingId;
        return fundingLock;
    }
}
