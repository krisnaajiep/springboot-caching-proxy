package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CachedOriginServiceTest extends OriginServiceTest {
    @Mock
    private ActualOriginService actualOriginService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private CachedOriginService cachedOriginService;

    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @AfterEach
    void tearDown() {
        super.tearDown();
    }

    @Test
    void forward_withNoCache_shouldForwardToActualServer() {
        when(restTemplate.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory(ORIGIN));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(redisTemplate.hasKey(anyString())).thenReturn(false);
        when(actualOriginService.forward(any(HttpMethod.class), anyString(), any())).thenReturn(cachedResponse);

        CachedResponse response = cachedOriginService.forward(HttpMethod.GET, "/resource", null);
        assertEquals(cachedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(cachedResponse.getHeaders(), response.getHeaders());
        assertArrayEquals(cachedResponse.getBody(), response.getBody());

        verify(restTemplate, times(1)).getUriTemplateHandler();
        verify(redisTemplate, times(2)).opsForValue();
        verify(valueOperations, times(1)).get(anyString());
        verify(redisTemplate, times(1)).hasKey(anyString());
        verify(actualOriginService, times(1)).forward(any(HttpMethod.class), anyString(), any());
        verify(valueOperations, times(1)).set(anyString(), any(CachedResponse.class));
        verifyNoMoreInteractions(restTemplate, redisTemplate, actualOriginService);
    }

    @Test
    void forward_withNullCache_shouldForwardToActualServer() {
        when(restTemplate.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory(ORIGIN));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(null);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);
        when(actualOriginService.forward(any(HttpMethod.class), anyString(), any())).thenReturn(cachedResponse);

        CachedResponse response = cachedOriginService.forward(HttpMethod.GET, "/resource", null);
        assertEquals(cachedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(cachedResponse.getHeaders(), response.getHeaders());
        assertArrayEquals(cachedResponse.getBody(), response.getBody());

        verify(restTemplate, times(1)).getUriTemplateHandler();
        verify(redisTemplate, times(2)).opsForValue();
        verify(valueOperations, times(1)).get(anyString());
        verify(redisTemplate, times(1)).hasKey(anyString());
        verify(actualOriginService, times(1)).forward(any(HttpMethod.class), anyString(), any());
        verify(valueOperations, times(1)).set(anyString(), any(CachedResponse.class));
        verifyNoMoreInteractions(restTemplate, redisTemplate, actualOriginService);
    }

    @Test
    void forward_withCache_shouldReturnCachedResponse() {
        when(restTemplate.getUriTemplateHandler()).thenReturn(new DefaultUriBuilderFactory(ORIGIN));
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get(anyString())).thenReturn(cachedResponse);
        when(redisTemplate.hasKey(anyString())).thenReturn(true);

        CachedResponse response = cachedOriginService.forward(HttpMethod.GET, "/resource", null);
        assertEquals(cachedResponse.getStatusCode(), response.getStatusCode());
        assertEquals(cachedResponse.getHeaders(), response.getHeaders());
        assertArrayEquals(cachedResponse.getBody(), response.getBody());

        verify(restTemplate, times(1)).getUriTemplateHandler();
        verify(redisTemplate, times(1)).opsForValue();
        verify(valueOperations, times(1)).get(anyString());
        verify(redisTemplate, times(1)).hasKey(anyString());
        verifyNoMoreInteractions(restTemplate, redisTemplate);
        verifyNoInteractions(actualOriginService);
    }


}