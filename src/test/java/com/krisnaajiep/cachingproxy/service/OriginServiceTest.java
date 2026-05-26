package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

abstract class OriginServiceTest {
    protected static final String ORIGIN = "https://origin.com";

    protected CachedResponse cachedResponse;

    @BeforeEach
    void setUp() {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "text/plain");
        cachedResponse = new CachedResponse(HttpStatusCode.valueOf(200), new HttpHeaders(headers), "body".getBytes());
    }

    @AfterEach
    void tearDown() {
        cachedResponse = null;
    }
}