package kcs.funding.fundingboost.domain.aop.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    String key();

    long expire() default 30000; // 기본 30초

    long waitTime() default 5000; // 기본 5초 대기 시간

    long retryInterval() default 100; // 기본 재시도 간격 100밀리초
}
