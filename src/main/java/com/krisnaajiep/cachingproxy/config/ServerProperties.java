package com.krisnaajiep.cachingproxy.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "server")
@Validated
public class ServerProperties {
    @NotNull
    private int port;

    @NotBlank
    private String origin;
}
