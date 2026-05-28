package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActualOriginServiceTest extends OriginServiceTest{
    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ResponseExtractor<CachedResponse> extractor;

    @InjectMocks
    private ActualOriginService actualOriginService;

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @AfterEach
    void tearDown() {
        super.tearDown();
    }

    @Test
    void forward_withCachedResponseExtractor_shouldReturnCachedResponse() {
        when(restTemplate.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory(ORIGIN));
        when(restTemplate.execute(any(URI.class), any(HttpMethod.class), isNull(), eq(extractor))).thenReturn(cachedResponse);

        CachedResponse response = actualOriginService.forward(HttpMethod.GET, "/resource", null);

        assertEquals(cachedResponse.getStatusCode(),  response.getStatusCode());
        assertEquals(cachedResponse.getHeaders(), response.getHeaders());
        assertArrayEquals(cachedResponse.getBody(), response.getBody());

        verify(restTemplate, times(1)).getUriTemplateHandler();
        verify(restTemplate, times(1)).execute(any(URI.class), any(HttpMethod.class), isNull(), eq(extractor));
        verifyNoMoreInteractions(restTemplate);
    }
}