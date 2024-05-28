package kcs.funding.fundingboost.domain.repository;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LockRepository {

    private final RedisTemplate lockTemplate;

    public LockRepository(@Qualifier("lockTemplate") RedisTemplate lockTemplate) {
        this.lockTemplate = lockTemplate;
    }

    public Boolean lock(Long fundingId) {
        return lockTemplate
                .opsForValue()
                .setIfAbsent(generateKey(fundingId), "lock", Duration.ofMillis(2_000));
    }

    public Boolean unlock(Long fundingId) {
        return lockTemplate.delete(generateKey(fundingId));
    }

    private String generateKey(Long id) {
        return String.valueOf(id);
    }
}

