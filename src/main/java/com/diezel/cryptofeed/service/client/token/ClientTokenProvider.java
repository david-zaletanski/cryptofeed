package com.diezel.cryptofeed.service.client.token;

import java.util.Date;

/**
 * An interface for a class that can provide valid JWT tokens and their expiration date.
 *
 * @author dzale
 */
public interface ClientTokenProvider {

    /**
     * Generates a ClientToken that contains the JWTToken String and related issue/expiry dates. Should
     * return null if for some reason generation of a valid token failed.
     *
     * @param issueDate  the date the token was issued
     * @param expiryDate the date the token expires and is no longer valid
     * @return a valid ClientToken if JWT token generation was successful, or null if unable
     */
    ClientToken generateToken(Date issueDate, Date expiryDate);

}
