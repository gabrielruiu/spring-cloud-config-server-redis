package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;

import java.util.Objects;

/**
 * {@link EnvironmentRepository} which retrieves properties from a Redis database.
 *
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
public class RedisEnvironmentRepository implements EnvironmentRepository {

    private static final String DEFAULT_LABEL = "master";

    private RedisConfigPropertySourceProvider redisConfigPropertySourceProvider;
    private ConfigServerProperties configServerProperties;

    @Autowired
    public RedisEnvironmentRepository(RedisConfigPropertySourceProvider redisConfigPropertySourceProvider, ConfigServerProperties configServerProperties) {
        this.redisConfigPropertySourceProvider = redisConfigPropertySourceProvider;
        this.configServerProperties = configServerProperties;
    }

    @Override
    public Environment findOne(String application, String inputProfileArray, String label) {
        application = fallbackToDefaultApplicationNameIfMissing(application);
        label = fallbackToDefaultLabelIfMissing(label);
        String profileArray = addDefaultProfileIfMissing(inputProfileArray);

        String[] profiles = profileArray.split(",");
        Environment environment = new Environment(application, inputProfileArray.split(","));

        for (String profile : profiles) {

            String globalApplicationName = configServerProperties.getDefaultApplicationName();
            PropertySource globalPropertySource = redisConfigPropertySourceProvider.getPropertySource(globalApplicationName, profile, label);
            if (globalPropertySource != null) {
                environment.add(globalPropertySource);
            }

            if (!Objects.equals(application, configServerProperties.getDefaultApplicationName())) {
                PropertySource applicationPropertySource = redisConfigPropertySourceProvider.getPropertySource(application, profile, label);
                if (applicationPropertySource != null) {
                    environment.add(applicationPropertySource);
                }
            }
        }

        return environment;
    }

    private String fallbackToDefaultLabelIfMissing(String label) {
        if (label == null) {
            if (configServerProperties.getDefaultLabel() != null) {
                return configServerProperties.getDefaultLabel();
            } else {
                return DEFAULT_LABEL;
            }
        }
        return label;
    }

    private String fallbackToDefaultApplicationNameIfMissing(String application) {
        if (application == null) {
            return configServerProperties.getDefaultApplicationName();
        }
        return application;
    }

    private String addDefaultProfileIfMissing(String profileArray) {
        if (profileArray == null) {
            return configServerProperties.getDefaultProfile();
        } else {
            String[] profiles = profileArray.split(",");

            boolean defaultProfileFound = false;
            for (String profile : profiles) {
                if (profile.equals(configServerProperties.getDefaultProfile())) {
                    defaultProfileFound = true;
                    break;
                }
            }
            if (!defaultProfileFound) {
                return profileArray + "," + configServerProperties.getDefaultProfile();
            }
            return profileArray;
        }
    }
}
