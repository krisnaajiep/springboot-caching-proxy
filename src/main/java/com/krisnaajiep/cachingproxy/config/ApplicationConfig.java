package com.krisnaajiep.cachingproxy.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
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
public class ApplicationConfig {
    private final ServerProperties serverProperties;
    private final RedisConnectionFactory redisConnectionFactory;

    @Bean
    public ApplicationListener<ApplicationStartedEvent> onApplicationStarted() {
        return event -> {
            ApplicationArguments args = new DefaultApplicationArguments(event.getArgs());

            if (args.containsOption("server.origin")) {
                List<String> optionValues = args.getOptionValues("server.origin");
                serverProperties.setOrigin(Objects.requireNonNull(optionValues).getFirst());
            }

            System.out.println("Welcome to the Spring Boot Caching Proxy Server with Interactive Shell\n");
            System.out.println("Server started on http://localhost:" + serverProperties.getPort());
            System.out.println("Server origin set to: " + serverProperties.getOrigin());
            System.out.println("\nType 'help' to see available commands.\n");
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
