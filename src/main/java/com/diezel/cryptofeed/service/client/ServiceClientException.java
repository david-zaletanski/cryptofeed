package com.diezel.cryptofeed.service.client;

import lombok.Data;

/**
 * A wrapper for any exceptions generated during ServiceClient calls, which will include information that
 * helps during debugging.
 *
 * @author dzale
 */
@Data
public class ServiceClientException extends Exception {

    private ServiceEndpoint serviceEndpoint;

    private Object requestObject;

    private String jwtToken;

    private Object[] uriVariables;

    public ServiceClientException(Exception cause, ServiceEndpoint serviceEndpoint, Object requestObject, String jwtToken, Object... uriVariables) {
        super(cause);
        this.serviceEndpoint = serviceEndpoint;
        this.requestObject = requestObject;
        this.jwtToken = jwtToken;
        this.uriVariables = uriVariables;
    }

    public ServiceClientException(String message, ServiceEndpoint serviceEndpoint, Object requestObject, String jwtToken, Object... uriVariables) {
        super(message);
        this.serviceEndpoint = serviceEndpoint;
        this.requestObject = requestObject;
        this.jwtToken = jwtToken;
        this.uriVariables = uriVariables;
    }

    public ServiceClientException(String message, Exception cause, ServiceEndpoint serviceEndpoint, Object requestObject, String jwtToken, Object... uriVariables) {
        super(message, cause);
        this.serviceEndpoint = serviceEndpoint;
        this.requestObject = requestObject;
        this.jwtToken = jwtToken;
        this.uriVariables = uriVariables;
    }

}
