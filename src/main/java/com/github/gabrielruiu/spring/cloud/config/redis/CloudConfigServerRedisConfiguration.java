package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.SearchPathLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@Configuration
@EnableConfigurationProperties(ConfigServerProperties.class)
public class CloudConfigServerRedisConfiguration {

    @Autowired
    private ConfigServerProperties configServerProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * As mentioned in the documentation, there is no support for serving plain text files
     */
    @Bean
    public SearchPathLocator searchPathLocator() {
        return new SearchPathLocator() {
            @Override
            public Locations getLocations(String application, String profile, String label) {
                return null;
            }
        };
    }

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
    public RedisPropertyNamePatternProvider redisConfigKeysUtilities() {
        return new RedisPropertyNamePatternProvider();
    }
}
