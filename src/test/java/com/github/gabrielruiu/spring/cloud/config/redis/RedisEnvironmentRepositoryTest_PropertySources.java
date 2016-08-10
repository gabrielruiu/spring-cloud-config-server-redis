package com.github.gabrielruiu.spring.cloud.config.redis;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.junit.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisEnvironmentRepositoryTest_PropertySources extends BaseRedisEnvironmentRepositoryTest {

    @Test
    public void shouldIncludeGlobalApplicationPropertiesIfPresent() {
        String profiles = "dev,mock-db,mock-client";
        Set<String> devKeys = Sets.newHashSet("application:dev:master:db:url", "application:dev:master:format:date");
        Set<String> mockDbKeys = Sets.newHashSet("application:mock-db:master:db:url", "application:mock-db:master:db:username", "application:mock-db:master:db:password");
        Set<String> mockClientKeys = Sets.newHashSet("application:mock-client:master:server:url");
        Set<String> globalApplicationKeys = Sets.newHashSet("application:default:master:formate:date");

        List<String> devProperties = Lists.newArrayList("http://localhost:3306/my-db", "dd/MM/yyyy");
        List<String> mockDbProperties = Lists.newArrayList("http://localhost:3306/my-mock-db", "my-user", "my-password");
        List<String> mockClientProperties = Lists.newArrayList("http://localhost:8080/my-rest-service");
        List<String> globalApplicationProperties = Lists.newArrayList("yyyy/MM/dd");

        String devKeyPattern = "application:dev:master:*";
        String mockDbKeyPattern = "application:mock-db:master:*";
        String mockClientKeyPattern = "application:mock-client:master:*";
        String globalApplicationKeyPattern = "application:default:master:*";

        given(stringRedisTemplate.keys(devKeyPattern)).willReturn(devKeys);
        given(stringRedisTemplate.keys(mockDbKeyPattern)).willReturn(mockDbKeys);
        given(stringRedisTemplate.keys(mockClientKeyPattern)).willReturn(mockClientKeys);
        given(stringRedisTemplate.keys(globalApplicationKeyPattern)).willReturn(globalApplicationKeys);

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.multiGet(devKeys)).willReturn(devProperties);
        given(valueOps.multiGet(mockDbKeys)).willReturn(mockDbProperties);
        given(valueOps.multiGet(mockClientKeys)).willReturn(mockClientProperties);
        given(valueOps.multiGet(globalApplicationKeys)).willReturn(globalApplicationProperties);

        String url = String.format("http://localhost:%d/application/%s/master", port, profiles);

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getProfiles(), arrayWithSize(4));
        assertThat(env.getProfiles(), arrayContaining("dev", "mock-db", "mock-client"));
        assertThat(env.getPropertySources(), notNullValue());
        assertThat(env.getPropertySources(), hasSize(4));
        assertThat(env.getPropertySources(), hasItem(devPropertySource()));
        assertThat(env.getPropertySources(), hasItem(mockDbPropertySource()));
        assertThat(env.getPropertySources(), hasItem(mockClientPropertySource()));
        assertThat(env.getPropertySources(), hasItem(globalApplicationPropertySource()));
    }

    @Test
    public void shouldReturnAPropertySourceForEachProfile() {
        String profiles = "dev,mock-db,mock-client";
        Set<String> devKeys = Sets.newHashSet("application:dev:master:db:url", "application:dev:master:format:date");
        Set<String> mockDbKeys = Sets.newHashSet("application:mock-db:master:db:url", "application:mock-db:master:db:username", "application:mock-db:master:db:password");
        Set<String> mockClientKeys = Sets.newHashSet("application:mock-client:master:server:url");

        List<String> devProperties = Lists.newArrayList("http://localhost:3306/my-db", "dd/MM/yyyy");
        List<String> mockDbProperties = Lists.newArrayList("http://localhost:3306/my-mock-db", "my-user", "my-password");
        List<String> mockClientProperties = Lists.newArrayList("http://localhost:8080/my-rest-service");

        String devKeyPattern = "application:dev:master:*";
        String mockDbKeyPattern = "application:mock-db:master:*";
        String mockClientKeyPattern = "application:mock-client:master:*";

        given(stringRedisTemplate.keys(devKeyPattern)).willReturn(devKeys);
        given(stringRedisTemplate.keys(mockDbKeyPattern)).willReturn(mockDbKeys);
        given(stringRedisTemplate.keys(mockClientKeyPattern)).willReturn(mockClientKeys);

        ValueOperations<String, String> valueOps = mock(ValueOperations.class);
        given(stringRedisTemplate.opsForValue()).willReturn(valueOps);
        given(valueOps.multiGet(devKeys)).willReturn(devProperties);
        given(valueOps.multiGet(mockDbKeys)).willReturn(mockDbProperties);
        given(valueOps.multiGet(mockClientKeys)).willReturn(mockClientProperties);

        String url = String.format("http://localhost:%d/application/%s/master", port, profiles);

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getProfiles(), arrayWithSize(3));
        assertThat(env.getProfiles(), arrayContaining("dev", "mock-db", "mock-client"));
        assertThat(env.getPropertySources(), notNullValue());
        assertThat(env.getPropertySources(), hasSize(3));
        assertThat(env.getPropertySources(), hasItem(devPropertySource()));
        assertThat(env.getPropertySources(), hasItem(mockDbPropertySource()));
        assertThat(env.getPropertySources(), hasItem(mockClientPropertySource()));
    }

    private PropertySource devPropertySource() {
        Map<String, String> devPropertyMap = new HashMap<>();
        devPropertyMap.put("db.url", "http://localhost:3306/my-db");
        devPropertyMap.put("format.date", "dd/MM/yyyy");
        return new PropertySource("application-dev", devPropertyMap);
    }

    private PropertySource mockDbPropertySource() {
        Map<String, String> mockDbPropertyMap = new HashMap<>();
        mockDbPropertyMap.put("db.url", "http://localhost:3306/my-mock-db");
        mockDbPropertyMap.put("db.username", "my-user");
        mockDbPropertyMap.put("db.password", "my-password");
        return new PropertySource("application-mock-db", mockDbPropertyMap);
    }

    private PropertySource mockClientPropertySource() {
        Map<String, String> mockClientPropertyMap = new HashMap<>();
        mockClientPropertyMap.put("server.url", "http://localhost:8080/my-rest-service");
        return new PropertySource("application-mock-client", mockClientPropertyMap);
    }

    private PropertySource globalApplicationPropertySource() {
        Map<String, String> devPropertyMap = new HashMap<>();
        devPropertyMap.put("format.date", "yyyy/MM/dd");
        return new PropertySource("application", devPropertyMap);
    }
}
