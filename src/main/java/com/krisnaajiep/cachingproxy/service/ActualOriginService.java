package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ActualOriginService implements OriginService {
    private final RestTemplate restTemplate;
    private final ResponseExtractor<CachedResponse> extractor;

    @Override
    public CachedResponse forward(HttpMethod method, String path, MultiValueMap<String, String> params) {
        URI uri = URI.create(path);
        String uriString = UriComponentsBuilder.fromUri(uri).queryParams(params).build().toUriString();
        URI expandedUri = restTemplate.getUriTemplateHandler().expand(uriString);

        return restTemplate.execute(expandedUri, method, null, extractor);
    }
}
