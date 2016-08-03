package com.github.gabrielruiu.spring.cloud.config.redis;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisConfigKeysUtilitiesTest {

    RedisConfigKeysUtilities keysUtilities = new RedisConfigKeysUtilities();

    @Test
    public void shouldReturnExpectedRedisKey() {
        String expectedKey = "application:default:master:*";

        String actualKey = keysUtilities.redisConfigKeyTemplate("application", "default", "master");

        assertThat(actualKey, is(expectedKey));
    }

    @Test
    public void shouldExtractThePropertyNameAndReplaceColonWithPeriod() {
        String expectedPropertyName = "format.date";

        String actualPropertyName = keysUtilities.formatKey("application", "default", "master",
                                                                            "application:default:master:format:date");

        assertThat(actualPropertyName, is(expectedPropertyName));
    }
}