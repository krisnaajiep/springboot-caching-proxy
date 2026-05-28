package com.krisnaajiep.cachingproxy.service;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

public interface OriginService {
    CachedResponse forward(HttpMethod method, String path, MultiValueMap<String, String> params);
}
