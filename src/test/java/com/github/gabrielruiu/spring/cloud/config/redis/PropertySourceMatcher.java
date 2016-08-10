package com.github.gabrielruiu.spring.cloud.config.redis;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.cloud.config.environment.PropertySource;

import java.util.Map;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class PropertySourceMatcher extends BaseMatcher<PropertySource> {

    private PropertySource expectedPropertySource;

    public PropertySourceMatcher(PropertySource expectedPropertySource) {
        this.expectedPropertySource = expectedPropertySource;
    }

    @Override
    public boolean matches(Object item) {
        if (item == null) {
            return false;
        }
        if (!(item instanceof PropertySource)) {
            return false;
        }
        PropertySource actualPropertySource = (PropertySource) item;
        if (expectedPropertySource.getName().equals(((PropertySource) item).getName())) {
            if (expectedPropertySource.getSource() == null && actualPropertySource.getSource() != null) {
                return false;
            }
            if (expectedPropertySource.getSource() != null && actualPropertySource.getSource() == null) {
                return false;
            }
            Map<String, String> expectedPropertyMap = (Map<String, String>) expectedPropertySource.getSource();
            Map<String, String> actualPropertyMap = (Map<String, String>) actualPropertySource.getSource();
            if (expectedPropertyMap.size() != actualPropertyMap.size()) {
                return false;
            }
            for (Map.Entry<String, String> actualPropertyEntrySet : actualPropertyMap.entrySet()) {
                if (expectedPropertyMap.containsKey(actualPropertyEntrySet.getKey())) {
                    if (!expectedPropertyMap.get(actualPropertyEntrySet.getKey()).equals(actualPropertyEntrySet.getValue())) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("PropertySource(name=")
                .appendText(expectedPropertySource.getName())
                .appendText(", source=")
                .appendValue(expectedPropertySource.getSource())
                .appendText(")");
    }

    public static PropertySourceMatcher matchingPropertySource(PropertySource propertySource) {
        return new PropertySourceMatcher(propertySource);
    }
}
