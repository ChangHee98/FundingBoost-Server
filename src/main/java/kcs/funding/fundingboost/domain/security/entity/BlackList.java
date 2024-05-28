package kcs.funding.fundingboost.domain.security.entity;

import static kcs.funding.fundingboost.domain.security.utils.SecurityConst.ACCESS_TOKEN_VALIDITY_IN_SECONDS;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value = "accessToken", timeToLive = ACCESS_TOKEN_VALIDITY_IN_SECONDS)
public class BlackList {

    @Id
    private String token;

    public static BlackList createBlackList(String accessToken) {
        BlackList blackList = new BlackList();
        blackList.token = accessToken;
        return blackList;
    }
}
