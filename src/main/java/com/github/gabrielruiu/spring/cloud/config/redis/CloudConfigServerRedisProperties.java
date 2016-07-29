package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@ConfigurationProperties(prefix = "spring.cloud.config.server.redis")
public class CloudConfigServerRedisProperties {

    /**
     * Flag to indicate whether Redis should be enabled as the storage for the configuration properties
     */
    private boolean enabled = true;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CloudConfigServerRedisProperties{");
        sb.append("enabled=").append(enabled);
        sb.append('}');
        return sb.toString();
    }
}
