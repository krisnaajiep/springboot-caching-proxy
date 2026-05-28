package com.krisnaajiep.cachingproxy.config;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import com.krisnaajiep.cachingproxy.response.CachedResponseExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final ServerProperties properties;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(properties.getOrigin()));
        return restTemplate;
    }

    @Bean
    public ResponseExtractor<CachedResponse> extractor() {
        return new CachedResponseExtractor();
    }
}
