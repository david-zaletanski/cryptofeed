package com.diezel.cryptofeed.service.client.token;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Manages creation and caching of JWT tokens included in the header when submitting requests
 * to secure endpoints.
 *
 * @author dzale
 */
public class ClientTokenManager {

    protected static final Logger log = LoggerFactory.getLogger(ClientTokenManager.class);

    /**
     * The currently cached JWT token.
     */
    private ClientToken token;

    /**
     * An object that can provide new JWT tokens.
     */
    private ClientTokenProvider tokenProvider;

    /**
     * Creates a ClientTokenManager object that will cache and create JWT tokens as needed for
     * ServiceClients calling secure endpoints.
     *
     * @param tokenProvider an object implementing the ClientTokenProvider interface that can create new JWT tokens
     */
    public ClientTokenManager(ClientTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
        this.token = null;
    }

    /**
     * Returns a valid and unexpired JWT token to be included in the HTTP Authorization: Bearer header
     * when submitting a request to a secure endpoint. Returns an empty string if an error
     * occurred during token generation.
     *
     * @return a valid JWT token String or an empty string if an error occurred
     */
    public String getToken() {
        if (token == null) {
            // If the token doesn't exist already, create it.
            log.trace("ClientTokenManager creating the initial JWT token.");
            return (createNewClientJWTToken() != null ? this.token.getToken() : "");
        } else if (!token.isExpired()) {
            // If we have an existing token that hasn't expired, use that.
            log.trace("ClientTokenManager returning valid cached token.");
            return this.token.getToken();
        } else {
            // If the cached token is expired, create a new one.
            log.trace("JWTTokenManager contains expired token, creating new token.");
            return (createNewClientJWTToken() != null ? this.token.getToken() : "");
        }
    }

    /**
     * Creates a new JWT token using the ClientTokenProvider, storing it in the cache, and returning it.
     *
     * @return the newly created and cached ClientToken, or null if generation failed
     */
    private ClientToken createNewClientJWTToken() {
        Date issueDate = new Date();
        this.token = tokenProvider.generateToken(issueDate);
        return this.token;
    }

}
