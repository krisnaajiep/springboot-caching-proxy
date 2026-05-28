package com.krisnaajiep.cachingproxy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CachedResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private HttpStatusCode statusCode;
    private HttpHeaders headers;
    private byte[] body;
}
