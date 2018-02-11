package com.diezel.cryptofeed.security;

import com.diezel.cryptofeed.security.constants.CryptofeedSecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A structure to hold the data of a JWT token.
 *
 * @author dzale
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CryptofeedUserToken {
    private static final Logger log = LoggerFactory.getLogger(CryptofeedUserToken.class);

    private String username;

    private List<String> roles;

    private Date issueDate;

    private Date expiryDate;

    /**
     * Recreate an existing CryptofeedUserToken out of data contained in an encoded JWT token string.
     * @param jwt valid jwt token string
     */
    public CryptofeedUserToken(String jwt) {
        CryptofeedUserToken token = fromJWT(jwt);
        this.username = token.getUsername();
        this.roles = token.getRoles();
        this.issueDate = token.getIssueDate();
        this.expiryDate = token.getExpiryDate();
    }

    /**
     * Create a new CryptofeedUserToken to be used for authentication. Sets issue date and default expiration date
     * based on constants.
     * @param username username that is authenticated
     * @param roles roles that the user has
     */
    public CryptofeedUserToken(String username, List<String> roles) {
        this.username = username;
        this.roles = roles;
        this.issueDate = new Date();
        this.expiryDate = addMillisecondsToDate(this.issueDate, CryptofeedSecurityConstants.MAX_TOKEN_LIFE_MILLISECONDS);
    }

    /**
     * Converts the data in this CryptofeedUserToken object to a JWT token string.
     * @return a JWT token string
     */
    public String toJWT() {
        return toJWT(this);
    }

    /**
     * Converts a CryptofeedUserToken object to a JWT token string.
     * @param token data to put in JWT token
     * @return a JWT token string
     */
    public static String toJWT(CryptofeedUserToken token) {
        // Create JWT Claims (extra data)
        Map<String, Object> claims = new HashMap<>();
        claims.put(CryptofeedSecurityConstants.JWT_CLAIMS_ROLES_KEY, token.getRoles());

        // Build token.
        String jwtToken = Jwts.builder()
                .setSubject(token.getUsername())
                .setClaims(claims)
                .setIssuedAt(token.getIssueDate())
                .setExpiration(token.getExpiryDate())
                .signWith(SignatureAlgorithm.HS512, getJWTSigningKeyBytes())
                .compact();

        return jwtToken;
    }

    private static byte[] getJWTSigningKeyBytes() {
        byte[] signingKeyBytes = null;
        try {
            signingKeyBytes = CryptofeedSecurityConstants.JWT_SIGNING_KEY
                    .getBytes(CryptofeedSecurityConstants.JWT_SIGNING_KEY_ENCODING);
        } catch (UnsupportedEncodingException ex) {
            log.error("Error converting Cryptofeed JWT signing key to byte array, encoding attempted '"+
                    CryptofeedSecurityConstants.JWT_SIGNING_KEY_ENCODING+"'.");
        }
        return signingKeyBytes;
    }

    private Date addMillisecondsToDate(Date originalDate, long milliseconds) {
        return new Date(originalDate.getTime() + milliseconds);
    }

    /**
     * Converts a JWT token string to a CryptofeedUserToken object.
     * @param jwt a JWT token string
     * @return a CryptofeedUserToken object
     */
    public static CryptofeedUserToken fromJWT(String jwt) {
        CryptofeedUserToken userToken = null;

        Jws<Claims> jwtClaims = Jwts.parser()
                .setSigningKey(getJWTSigningKeyBytes())
                .parseClaimsJws(jwt);

        String username = jwtClaims.getBody().getSubject();
        List<String> roles = (List<String>)jwtClaims.getBody().get(CryptofeedSecurityConstants.JWT_CLAIMS_ROLES_KEY);
        Date issueDate = jwtClaims.getBody().getIssuedAt();
        Date expiryDate = jwtClaims.getBody().getExpiration();

        userToken = CryptofeedUserToken.builder()
                .username(username)
                .roles(roles)
                .issueDate(issueDate)
                .expiryDate(expiryDate)
                .build();

        return userToken;
    }
}
