package com.github.gabrielruiu.spring.cloud.config.redis;

/**
 * Handles the representation of the property names within Redis.
 *
 * The key in Redis contains the following information, in the specified order:
 * 1. application name
 * 2. profile
 * 3. label
 * 4. property name
 *
 * For example, we have the following key:
 * "roles-engine:default:master:url"
 *
 * From which we resolve the following values:
 * 1. application name = roles-engine
 * 2. profile = default
 * 3. label = master
 * 4. property name = url
 *
 * Note that anything after the first three parts of the key will be considered
 * the property name, even if there's more than one part.
 *
 * Example:
 *
 * The key "roles-engine:default:master:format:date" would translate to the same values as above
 * for the application name, profile and label, but the property name would resolve to
 * "format.date".
 * In this case, the colon (:) will be replaced by a period (.), in order to keep the same format
 * use in regular property files.
 *
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisPropertyNamePatternProvider {

    private static final String KEY_FORMAT = "%s:%s:%s:";

    /**
     * Use the application name, profile and label to construct a key pattern used to
     * search for the properties in Redis
     *
     * @see <a href="http://redis.io/commands/KEYS">Redis KEYS command</a>
     */
    public String generateKeyPattern(String application, String profile, String label) {
        return String.format(KEY_FORMAT + "*", application, profile, label);
    }

    /**
     * Takes the property key how it's represented in the Redis database, and extracts the property name
     *
     * Example:
     *
     * The key "roles-engine:default:master:format:date" would be resolved to "format.date"
     */
    public String formatRedisKeyIntoPropertyName(String application, String profile, String label, String key) {
        String extractedPropertyName = key.replace(String.format(KEY_FORMAT, application, profile, label), "");
        return extractedPropertyName.replace(":", ".");
    }
}
