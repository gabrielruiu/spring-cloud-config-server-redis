package com.github.gabrielruiu.spring.cloud.config.redis;

import org.junit.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisEnvironmentRepositoryTest_Basic extends BaseRedisEnvironmentRepositoryTest {

    @Test
    public void shouldRetrieveEnvironment() {
        Map<String, String> properties = new HashMap<>();
        properties.put("application:default:master:format:date", "dd/MM/yyyy");
        properties.put("application:default:master:url", "http://localhost:8080/url");
        injectPropertiesIntoRedis(properties);
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
