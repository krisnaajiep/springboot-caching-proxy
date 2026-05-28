package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class CachedOriginService implements OriginService {
    private final ActualOriginService actualOriginService;
    private final RestTemplate restTemplate;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public CachedResponse forward(HttpMethod method, String path, MultiValueMap<String, String> params) {
        String cacheKey = buildCacheKey(method, path, params);
        CachedResponse cachedResponse = (CachedResponse) redisTemplate.opsForValue().get(cacheKey);

        if (!redisTemplate.hasKey(cacheKey) || cachedResponse == null) {
            CachedResponse response = actualOriginService.forward(method, path, params);
            response.getHeaders().set("X-Cache", "MISS");
            redisTemplate.opsForValue().set(cacheKey, response);
            return response;
        }

        cachedResponse.getHeaders().set("X-Cache", "HIT");

        return cachedResponse;
    }

    private String buildCacheKey(HttpMethod method, String path, MultiValueMap<String, String> params) {
        String uriString = UriComponentsBuilder.fromUriString(path).queryParams(params).build().toUriString();
        URI expandedUri = restTemplate.getUriTemplateHandler().expand(":" + uriString);

        return method.name() + ":" + expandedUri;

    }
}
