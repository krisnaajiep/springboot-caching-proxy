package com.krisnaajiep.cachingproxy.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.shell.core.command.Command;

import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ServerConfig {
    private final ServerProperties serverProperties;
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ApplicationRunner setServerOrigin() {
        return args -> {
            if (!args.containsOption("server.origin")) {
                return;
            }

            List<String> optionValues = args.getOptionValues("server.origin");
            if (Objects.isNull(optionValues) || optionValues.size() != 1 || optionValues.getFirst().isBlank()) {
                throw new IllegalArgumentException("Invalid server origin value. Please provide a value for --server.origin");
            }

            serverProperties.setOrigin(optionValues.getFirst());
            log.info("Server origin set to: {}", serverProperties.getOrigin());
        };
    }

    @Bean
    public Command clearCache() {
        return Command.builder()
                .name("clear-cache")
                .description("Clear the Redis cache")
                .execute(context -> {
                    try (PrintWriter printWriter = context.outputWriter()) {
                        printWriter.println("Clearing all cache data...");
                        redisConnectionFactory.getConnection().serverCommands().flushDb();
                        printWriter.println("Cache cleared successfully.");
                    }
                });
    }
}
