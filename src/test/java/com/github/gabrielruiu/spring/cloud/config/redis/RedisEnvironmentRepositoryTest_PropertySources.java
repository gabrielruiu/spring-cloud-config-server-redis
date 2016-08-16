package com.github.gabrielruiu.spring.cloud.config.redis;

import org.junit.Test;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;

import java.util.HashMap;
import java.util.Map;

import static com.github.gabrielruiu.spring.cloud.config.redis.PropertySourceBuilder.*;
import static com.github.gabrielruiu.spring.cloud.config.redis.PropertySourceMatcher.matchingPropertySource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisEnvironmentRepositoryTest_PropertySources extends BaseRedisEnvironmentRepositoryTest {

    @Test
    public void shouldIncludeGlobalApplicationPropertiesIfPresent() {
        String profiles = "dev,mock-db,mock-client";
        Map<String, String> properties = getBasicProperties();
        properties.put("application:default:master:format:date", "yyyy/MM/dd");
        injectPropertiesIntoRedis(properties);

        String url = String.format("http://localhost:%d/application/%s/master", port, profiles);

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getProfiles(), arrayWithSize(3));
        assertThat(env.getProfiles(), arrayContaining("dev", "mock-db", "mock-client"));
        assertThat(env.getPropertySources(), notNullValue());
        assertThat(env.getPropertySources(), hasSize(4));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(devPropertySource())));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(mockDbPropertySource())));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(mockClientPropertySource())));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(globalApplicationPropertySource())));
    }

    @Test
    public void shouldIncludePropertiesForDefaultProfile() {
        String profiles = "dev";
        Map<String, String> basicProperties = getBasicProperties();
        basicProperties.putAll(PropertySourceBuilder.applicationSpecificProperties());
        injectPropertiesIntoRedis(basicProperties);
        String url = String.format("http://localhost:%d/my-app/%s/master", port, profiles);
        PropertySource defaultMyAppPropertySource = aPropertySource().withName("my-app").withProperty("format.date", "MM/dd/yyyy").build();
        PropertySource devMyAppPropertySource = aPropertySource().withName("my-app-dev")
                                                      .withProperty("format.date", "yyyy/MM/dd")
                                                      .withProperty("url", "http://dev.api.rest:10000/rest").build();
        PropertySource devApplicationPropertySource = aPropertySource().withName("application-dev")
                                                      .withProperty("format.date", "dd/MM/yyyy")
                                                      .withProperty("db.url", "http://localhost:3306/my-db").build();

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getPropertySources(), notNullValue());
        assertThat(env.getPropertySources(), hasSize(3));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(devApplicationPropertySource)));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(defaultMyAppPropertySource)));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(devMyAppPropertySource)));
    }

    @Test
    public void shouldReturnAPropertySourceForEachProfile() {
        String profiles = "dev,mock-db,mock-client";
        injectPropertiesIntoRedis(getBasicProperties());

        String url = String.format("http://localhost:%d/application/%s/master", port, profiles);

        Environment env = testRestTemplate.getForObject(url, Environment.class);

        assertThat(env, notNullValue());
        assertThat(env.getProfiles(), arrayWithSize(3));
        assertThat(env.getProfiles(), arrayContaining("dev", "mock-db", "mock-client"));
        assertThat(env.getPropertySources(), notNullValue());
        assertThat(env.getPropertySources(), hasSize(3));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(devPropertySource())));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(mockDbPropertySource())));
        assertThat(env.getPropertySources(), hasItem(matchingPropertySource(mockClientPropertySource())));
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
