package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisConfigPropertySourceProvider {

    private StringRedisTemplate stringRedisTemplate;
    private RedisConfigKeysProvider redisConfigKeysProvider;
    private RedisConfigKeysUtilities redisConfigKeysUtilities;
    private ConfigServerProperties configServerProperties;

    @Autowired
    public RedisConfigPropertySourceProvider(StringRedisTemplate stringRedisTemplate, RedisConfigKeysProvider redisConfigKeysProvider, RedisConfigKeysUtilities redisConfigKeysUtilities, ConfigServerProperties configServerProperties) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisConfigKeysProvider = redisConfigKeysProvider;
        this.redisConfigKeysUtilities = redisConfigKeysUtilities;
        this.configServerProperties = configServerProperties;
    }

    public PropertySource getPropertySource(String application, String profile, String label) {
        List<String> keys = new ArrayList<>(redisConfigKeysProvider.getKeys(application, profile, label));
        if (keys.size() > 0) {
            List<String> propertyValues = stringRedisTemplate.opsForValue().multiGet(keys);

            Map<String, String> properties = new HashMap<>();
            for (int i=0; i<keys.size(); i++) {
                properties.put(formatKey(application, profile, label, keys.get(i)),
                               propertyValues.get(i));
            }
            return new PropertySource(getPropertySourceName(application, profile), properties);
        }
        return null;
    }

    private String formatKey(String application, String profile, String label, String key) {
        String extractedPropertyName = redisConfigKeysUtilities.extractPropertyNameNameFromKey(application, profile, label, key);
        return extractedPropertyName.replace(":", ".");
    }

    private String getPropertySourceName(String application, String profile) {
        if (!Objects.equals(profile, configServerProperties.getDefaultProfile())) {
            return application + "-" + profile;
        }
        return application;
    }
}
