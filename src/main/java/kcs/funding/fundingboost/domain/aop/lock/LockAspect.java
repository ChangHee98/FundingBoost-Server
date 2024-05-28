package kcs.funding.fundingboost.domain.aop.lock;

import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class LockAspect {

    private final @Qualifier("lockTemplate") RedisTemplate<String, String> lockTemplate;

    public LockAspect(RedisTemplate<String, String> lockTemplate) {
        this.lockTemplate = lockTemplate;
    }

    @Around("@annotation(redisLock)")
    public Object execute(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
        log.info("lock 획득 시도");
        String lockKey = redisLock.key();
        long expire = redisLock.expire();
        long waitTime = redisLock.waitTime();
        long retryInterval = redisLock.retryInterval();

        long startTime = System.currentTimeMillis();
        boolean acquired = false;

        while (!acquired && (System.currentTimeMillis() - startTime) < waitTime) {
            acquired = Boolean.TRUE.equals(
                    lockTemplate.opsForValue().setIfAbsent(lockKey, "locked", expire, TimeUnit.MILLISECONDS));
            if (!acquired) {
                Thread.sleep(retryInterval); // 잠시 대기 후 재시도
            } else {
                log.info("lock 획득");
            }
        }

        if (acquired) {
            try {
                return joinPoint.proceed(); // 실제 메서드 실행
            } finally {
                lockTemplate.delete(lockKey); // 락 해제
                log.info("lock 반납");
            }
        } else {
            throw new RuntimeException("Unable to acquire lock for key: " + lockKey);
        }
    }
}
