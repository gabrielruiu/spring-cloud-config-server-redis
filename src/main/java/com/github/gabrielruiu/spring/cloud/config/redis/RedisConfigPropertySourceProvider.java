package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

/**
 * Handles retrieving and formatting of Redis properties, based on the
 * application name, profile and label
 *
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisConfigPropertySourceProvider {

    private StringRedisTemplate stringRedisTemplate;
    private RedisConfigKeysProvider redisConfigKeysProvider;
    private RedisPropertyNamePatternProvider redisPropertyNamePatternProvider;
    private ConfigServerProperties configServerProperties;

    @Autowired
    public RedisConfigPropertySourceProvider(StringRedisTemplate stringRedisTemplate, RedisConfigKeysProvider redisConfigKeysProvider, RedisPropertyNamePatternProvider redisPropertyNamePatternProvider, ConfigServerProperties configServerProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisConfigKeysProvider = redisConfigKeysProvider;
        this.redisPropertyNamePatternProvider = redisPropertyNamePatternProvider;
        this.configServerProperties = configServerProperties;
    }

    public PropertySource getPropertySource(String application, String profile, String label) {
        Set<String> keys = redisConfigKeysProvider.getKeys(application, profile, label);
        if (keys.size() > 0) {
            List<String> propertyValues = stringRedisTemplate.opsForValue().multiGet(keys);

            Map<String, String> properties = new HashMap<>();
            int i = 0;
            for (String key : keys) {
                String propertyName = formatKey(application, profile, label, key);
                String propertyValue = propertyValues.get(i);
                properties.put(propertyName, propertyValue);
                i++;
            }
            return new PropertySource(getPropertySourceName(application, profile), properties);
        }
        return null;
    }

    private String formatKey(String application, String profile, String label, String key) {
        return redisPropertyNamePatternProvider.formatRedisKeyIntoPropertyName(application, profile, label, key);
    }

    private String getPropertySourceName(String application, String profile) {
        if (!Objects.equals(profile, configServerProperties.getDefaultProfile())) {
            return application + "-" + profile;
        }
        return application;
    }
}
