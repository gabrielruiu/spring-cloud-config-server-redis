package com.github.gabrielruiu.spring.cloud.config.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RedisEnvironmentRepositoryTest {

    @LocalServerPort
    int port;

    @MockBean
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    public void shouldRetrieveEnvironment() {
        String pattern = "application:default:master:*";
        List<String> properties = Lists.newArrayList("http://localhost:8080/url", "dd/MM/yyyy");
        Set<String> keys = Sets.newHashSet("application:default:master:url", "application:default:master:format:date");
        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        given(stringRedisTemplate.keys(pattern)).willReturn(keys);
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
        assertThat(propertySource.getSource(), hasEntry("format.date", "dd/MM/yyyy"));
        assertThat(propertySource.getSource(), hasEntry("url", "http://localhost:8080/url"));
    }
}
