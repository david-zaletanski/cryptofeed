package com.diezel.cryptofeed.service.client.token;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Holds the informtaion about a JWT token that a client needs to know.
 *
 * @author dzale
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientToken {

    /**
     * The JWT token String to be included in HTTP Authorization: Bearer header.
     */
    private String token;

    /**
     * The date / time that the JWT token was issued.
     */
    private Date issueDate;

    /**
     * The date / time that the JWT token will expire.
     */
    private Date expirationDate;

    /**
     * Checks if the current date/time is after the expiration date. If so, the token is expired.
     *
     * @return true if the token is expired
     */
    public boolean isExpired() {
        return new Date().after(expirationDate);
    }

}
