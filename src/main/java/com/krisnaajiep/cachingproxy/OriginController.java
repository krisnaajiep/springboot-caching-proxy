package com.krisnaajiep.cachingproxy;

import com.krisnaajiep.cachingproxy.response.CachedResponse;
import com.krisnaajiep.cachingproxy.service.OriginService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OriginController {
    private final OriginService originService;

    public OriginController(@Qualifier("cachedOriginService") OriginService originService) {
        this.originService = originService;
    }

    @GetMapping("/{*path}")
    public ResponseEntity<String> get(
            @PathVariable String path,
            @RequestParam(required = false) MultiValueMap<String, String> params
    ) {
        CachedResponse response = originService.forward(HttpMethod.GET, path, params);

        return ResponseEntity.status(response.getStatusCode())
                .headers(response.getHeaders())
                .body(new String(response.getBody()));
    }
}
