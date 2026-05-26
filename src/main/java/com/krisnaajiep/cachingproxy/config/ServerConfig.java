package com.krisnaajiep.cachingproxy.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ServerConfig {
    private final ServerProperties properties;

    @Bean
    public ApplicationRunner run() {
        return args -> {
            if (args.containsOption("origin")) {
                List<String> originList = args.getOptionValues("origin");

                if (Objects.nonNull(originList) && !originList.isEmpty()) {
                    properties.setOrigin(originList.getFirst());
                }
            }

            log.info("Server origin is {}", properties.getOrigin());
        };
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
