package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.cloud.config.server.environment.SearchPathLocator;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@Component
public class NoopSearchPathLocator implements SearchPathLocator {

    @Override
    public Locations getLocations(String application, String profile, String label) {
        return new Locations(application, profile, label, UUID.randomUUID().toString(), new String[]{});
    }
}
