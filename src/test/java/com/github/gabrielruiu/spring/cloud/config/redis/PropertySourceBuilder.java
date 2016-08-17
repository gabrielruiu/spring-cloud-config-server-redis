package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.cloud.config.environment.PropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class PropertySourceBuilder {

    private String name;
    private Map<String, String> propertyMap = new HashMap<>();

    public static PropertySourceBuilder aPropertySource() {
        return new PropertySourceBuilder();
    }

    public PropertySourceBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PropertySourceBuilder withProperty(String key, String value) {
        this.propertyMap.put(key, value);
        return this;
    }

    public PropertySource build() {
        return new PropertySource(name, propertyMap);
    }

    public static Map<String, String> getBasicProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("application:dev:master:format:date", "dd/MM/yyyy");
        properties.put("application:dev:master:db:url", "http://localhost:3306/my-db");
        properties.put("application:mock-db:master:db:url", "http://localhost:3306/my-mock-db");
        properties.put("application:mock-db:master:db:username", "my-user");
        properties.put("application:mock-db:master:db:password", "my-password");
        properties.put("application:mock-client:master:server:url", "http://localhost:8080/my-rest-service");
        return properties;
    }

    public static Map<String, String> applicationSpecificProperties() {
        Map<String, String> appSpecificProperties = new HashMap<>();
        appSpecificProperties.put("my-app:default:master:format:date", "MM/dd/yyyy");
        appSpecificProperties.put("my-app:dev:master:format:date", "yyyy/MM/dd");
        appSpecificProperties.put("my-app:dev:master:url", "http://dev.api.rest:10000/rest");
        return appSpecificProperties;
    }

    public static PropertySource devPropertySource() {
        Map<String, String> devPropertyMap = new HashMap<>();
        devPropertyMap.put("db.url", "http://localhost:3306/my-db");
        devPropertyMap.put("format.date", "dd/MM/yyyy");
        return new PropertySource("application-dev", devPropertyMap);
    }

    public static PropertySource mockDbPropertySource() {
        Map<String, String> mockDbPropertyMap = new HashMap<>();
        mockDbPropertyMap.put("db.url", "http://localhost:3306/my-mock-db");
        mockDbPropertyMap.put("db.username", "my-user");
        mockDbPropertyMap.put("db.password", "my-password");
        return new PropertySource("application-mock-db", mockDbPropertyMap);
    }

    public static PropertySource mockClientPropertySource() {
        Map<String, String> mockClientPropertyMap = new HashMap<>();
        mockClientPropertyMap.put("server.url", "http://localhost:8080/my-rest-service");
        return new PropertySource("application-mock-client", mockClientPropertyMap);
    }

    public static PropertySource globalApplicationPropertySource() {
        Map<String, String> devPropertyMap = new HashMap<>();
        devPropertyMap.put("format.date", "yyyy/MM/dd");
        return new PropertySource("application", devPropertyMap);
    }
}
