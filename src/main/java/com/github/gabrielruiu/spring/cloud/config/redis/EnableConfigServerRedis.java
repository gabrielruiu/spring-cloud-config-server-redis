package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.cloud.config.server.config.ConfigServerEncryptionConfiguration;
import org.springframework.cloud.config.server.config.ConfigServerMvcConfiguration;
import org.springframework.cloud.config.server.config.ResourceRepositoryConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({ConfigServerEncryptionConfiguration.class, ConfigServerMvcConfiguration.class,
        CloudConfigServerRedis.class, ResourceRepositoryConfiguration.class})
public @interface EnableConfigServerRedis {
}
