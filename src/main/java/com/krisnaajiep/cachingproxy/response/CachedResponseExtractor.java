package com.krisnaajiep.cachingproxy.response;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;

public class CachedResponseExtractor implements ResponseExtractor<CachedResponse> {
    @Override
    public @Nullable CachedResponse extractData(@NonNull ClientHttpResponse response) throws IOException {
        return CachedResponse.builder()
                .statusCode(response.getStatusCode())
                .headers(response.getHeaders())
                .body(response.getBody().readAllBytes())
                .build();
    }
}
