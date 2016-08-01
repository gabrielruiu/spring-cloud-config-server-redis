package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.cloud.config.server.environment.SearchPathLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@ConditionalOnClass(StringRedisTemplate.class)
@ConditionalOnMissingBean(EnvironmentRepository.class)
@ConditionalOnProperty(value = "spring.cloud.config.server.redis.enabled", havingValue = "true")
@Configuration
public class CloudConfigServerRedisAutoConfiguration {

    @Autowired
    private ConfigServerProperties configServerProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public RedisEnvironmentRepository redisEnvironmentRepository() {
        return new RedisEnvironmentRepository(redisConfigPropertySourceProvider(), configServerProperties);
    }

    @Bean
    public RedisConfigPropertySourceProvider redisConfigPropertySourceProvider() {
        return new RedisConfigPropertySourceProvider(stringRedisTemplate, redisConfigKeysProvider(), redisConfigKeysUtilities(), configServerProperties);
    }

    @Bean
    public RedisConfigKeysProvider redisConfigKeysProvider() {
        return new RedisConfigKeysProvider(stringRedisTemplate, redisConfigKeysUtilities());
    }

    @Bean
    public RedisConfigKeysUtilities redisConfigKeysUtilities() {
        return new RedisConfigKeysUtilities();
    }

    @Bean
    @ConditionalOnMissingBean(SearchPathLocator.class)
    public SearchPathLocator searchPathLocator() {
        return new NoopSearchPathLocator();
    }
}
