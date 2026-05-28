package com.krisnaajiep.cachingproxy.config;

import com.redis.testcontainers.RedisContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

public interface ITContainers {
    @Container
    @ServiceConnection(name = "redis")
    RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("redis:8.6.2"));
}
