package com.diezel.cryptofeed.service.client;

import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;

/**
 * Storage for a complete service endpoint URL.
 *
 * @author dzale
 */
@Data
public class ServiceEndpoint {

    @NotNull
    private final String baseUrl;

    @NotNull
    private final String endpoint;

    @NotNull
    private final HttpMethod httpMethod;

    public String getCompleteUrl() {
        return baseUrl + endpoint;
    }

    @Override
    public String toString() {
        return "[" + httpMethod.toString() + "] " + baseUrl + endpoint;
    }
}
