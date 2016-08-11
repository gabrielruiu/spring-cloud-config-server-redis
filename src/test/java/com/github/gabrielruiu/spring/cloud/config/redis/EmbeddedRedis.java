package com.github.gabrielruiu.spring.cloud.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Component;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * @author Gabriel Mihai Ruiu (gabriel.ruiu@mail.com)
 */
@Component
@SuppressWarnings("unused")
public class EmbeddedRedis {

    @Autowired
    RedisProperties redisProperties;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException {
        redisServer = new RedisServer(redisProperties.getPort());
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
