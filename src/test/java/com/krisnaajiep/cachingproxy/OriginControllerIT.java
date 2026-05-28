package com.krisnaajiep.cachingproxy;

import com.krisnaajiep.cachingproxy.config.ITConfig;
import com.krisnaajiep.cachingproxy.response.CachedResponse;
import com.krisnaajiep.cachingproxy.service.ActualOriginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(ITConfig.class)
class OriginControllerIT {
    @MockitoBean
    private ActualOriginService actualOriginService;

    protected CachedResponse cachedResponse;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @BeforeEach
    void setUp() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().serverCommands().flushDb();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "text/plain");
        cachedResponse = new CachedResponse(HttpStatusCode.valueOf(200), new HttpHeaders(headers), "body".getBytes());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void get_withSingleCall_shouldCacheMiss() throws Exception {
        when(actualOriginService.forward(any(HttpMethod.class), anyString(), any())).thenReturn(cachedResponse);

        MvcResult result = mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Cache", "MISS"))
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull(content);

        verify(actualOriginService, times(1)).forward(any(HttpMethod.class), anyString(), any());
        verifyNoMoreInteractions(actualOriginService);
    }

    @Test
    void get_withMultipleCall_shouldCacheMissThenCacheHit() throws Exception {
        when(actualOriginService.forward(any(HttpMethod.class), anyString(), any())).thenReturn(cachedResponse);

        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Cache", "MISS"));

        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Cache", "HIT"));

        verify(actualOriginService, times(1)).forward(any(HttpMethod.class), anyString(), any());
    }
}