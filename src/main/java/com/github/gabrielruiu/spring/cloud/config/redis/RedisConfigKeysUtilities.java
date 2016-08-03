package com.github.gabrielruiu.spring.cloud.config.redis;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisConfigKeysUtilities {

    private static final String KEY_FORMAT = "%s:%s:%s:";

    public String redisConfigKeyTemplate(String application, String profile, String label) {
        return String.format(KEY_FORMAT + "*", application, profile, label);
    }

    public String formatKey(String application, String profile, String label, String key) {
        String extractedPropertyName = key.replace(String.format(KEY_FORMAT, application, profile, label), "");
        return extractedPropertyName.replace(":", ".");
    }
}
