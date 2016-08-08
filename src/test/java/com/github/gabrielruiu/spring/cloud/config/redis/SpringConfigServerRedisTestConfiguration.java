package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@EnableConfigServerRedis
@SpringBootApplication
public class SpringConfigServerRedisTestConfiguration {

    public static void main(String[] args) {
        SpringApplication.run(SpringConfigServerRedisTestConfiguration.class, args);
    }
}
