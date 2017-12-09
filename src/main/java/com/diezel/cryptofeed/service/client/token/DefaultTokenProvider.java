package com.diezel.cryptofeed.service.client.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * A default token provider that generates an token as an example.
 *
 * @author dzale
 */
public class DefaultTokenProvider implements ClientTokenProvider {

    /**
     * Tokens are signed with this signing key by default.
     */
    public static final String DEFAULT_SIGNING_KEY = "ABC123ABC456";
    private static final Logger log = LoggerFactory.getLogger(DefaultTokenProvider.class);

    @Override
    public ClientToken generateToken(Date issueDate, Date expiryDate) {

        // Create the token.
        String jwtToken = createJwtToken(DEFAULT_SIGNING_KEY, issueDate, expiryDate);

        // If there was an exception or error creating jwtToken, return null.
        if (jwtToken == null)
            return null;

        // Return token as part of package in ClientToken
        return ClientToken.builder()
                .issueDate(issueDate)
                .token(jwtToken)
                .expirationDate(expiryDate)
                .build();
    }

    private String createJwtToken(String signingKey, Date issueDate, Date expiryDate) {
        String compactJwt = "";
        try {
            compactJwt = Jwts.builder()
                    .setSubject("Default")
                    .setIssuedAt(issueDate)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, signingKey)
                    .compact();
        } catch (Exception ex) {
            log.error("Exception while generating JWT token.", ex);
        }

        return compactJwt;
    }

}
