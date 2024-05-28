package kcs.funding.fundingboost.domain.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
public class LockConfig {
    @Value("${spring.data.redis.lock.host}")
    private String host;

    @Value("${spring.data.redis.lock.port}")
    private int port;

    @Bean
    @Qualifier("lockRedisConnectionFactory")
    public RedisConnectionFactory lockRedisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    @Qualifier("lockTemplate")
    public RedisTemplate<String, String> lockTemplate(
            @Qualifier("lockRedisConnectionFactory") RedisConnectionFactory lockRedisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(lockRedisConnectionFactory());
        return redisTemplate;
    }
}
