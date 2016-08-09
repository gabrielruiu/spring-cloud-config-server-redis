package com.github.gabrielruiu.spring.cloud.config.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisEnvironmentRepositoryTest_Basic extends BaseRedisEnvironmentRepositoryTest {

    private static final String KEY_BASE = "application:default:master:";
    private static final String KEY_PATTERN = KEY_BASE + "*";
    private static final String PROPERTY1 = KEY_BASE + "url";
    private static final String PROPERTY2 = KEY_BASE + "format:date";
    private static final String PROPERTY1_VALUE = "http://localhost:8080/url";
    private static final String PROPERTY2_VALUE = "dd/MM/yyyy";

    @Test
    public void shouldRetrieveEnvironment() {
        List<String> properties = Lists.newArrayList(PROPERTY1_VALUE, PROPERTY2_VALUE);
        Set<String> keys = Sets.newHashSet(PROPERTY1, PROPERTY2);
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        given(stringRedisTemplate.keys(KEY_PATTERN)).willReturn(keys);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.multiGet(keys)).willReturn(properties);
        String url = String.format("http://localhost:%d/application/default/master", port);

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getName(), is("application"));
        assertThat(env.getVersion(), nullValue());
        assertThat(env.getProfiles(), arrayWithSize(1));
        assertThat(env.getLabel(), is("master"));
        assertThat(env.getPropertySources(), hasSize(1));
        PropertySource propertySource = env.getPropertySources().get(0);
        assertThat(propertySource, notNullValue());
        assertThat(propertySource.getName(), is("application"));
        assertThat(propertySource.getSource(), notNullValue());
        assertThat(propertySource.getSource(), hasEntry("format.date", PROPERTY2_VALUE));
        assertThat(propertySource.getSource(), hasEntry("url", PROPERTY1_VALUE));
    }
}
