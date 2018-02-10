package com.diezel.cryptofeed.vendor.kraken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Responsible for generating the HTTP headers to attach to Kraken API requests.
 *
 * @author dzale
 */
@Component
public class KrakenHTTPHeader {

    private static final Logger log = LoggerFactory.getLogger(KrakenHTTPHeader.class);

    // HTTP Header Names
    private final String HTTP_HEADER_API_KEY = "API-Key";
    private final String HTTP_HEADER_API_SIGN = "API-Sign";

    // Encryption Names
    private final String HMAC_SHA512 = "HmacSHA512";
    private final String SHA256 = "SHA-256";
    private final String UTF8 = "UTF-8";

    // User Values
    @Value("cryptofeed.vendor.kraken.apiKey")
    private String publicApiKey;

    @Value("cryptofeed.vendor.kraken.apiSecretKey")
    private String privateApiKey;

    /**
     * Returns an HttpHeader object needed to attach to the API request.
     * @param uriPath
     * @return
     */
    public HttpHeaders getKrakenApiRequestHeaders(String uriPath) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HTTP_HEADER_API_KEY, getHttpApiKeyValue());
        headers.add(HTTP_HEADER_API_SIGN, getHttpApiSignValue(uriPath));

        return headers;
    }

    /**
     * Returns the API-Key HTTP header value.
     * @return string with api key
     */
    private String getHttpApiKeyValue() {
        return publicApiKey;
    }

    /**
     * Returns the value for the API-Sign HTTP header.
     * @param uriPath the target URL of API call
     * @return the API-Sign value as String
     */
    private String getHttpApiSignValue(String uriPath) {
        return calculateApiSignValue(uriPath);
    }

    /**
     * Calculates the API-Sign value for the HTTP headers. The following is from Kraken API documentation:

     * API-Sign = Message signature using HMAC-SHA512 of (URI path + SHA256(nonce + POST data)) and base64 decoded secret API key
     * Post Data =
     *              nonce = always increasing unsigned 64 bit integer
     *              otp = two-factor password (if two-factor enabled, otherwise not required)
     *
     * Note: There is no way to reset the nonce to a lower value so be sure to use a nonce generation method that won't generate numbers less than the previous nonce. A persistent counter or the current time in hundredths of a second precision or higher is suggested. Too many requests with nonces below the last valid nonce (EAPI:Invalid nonce) can result in temporary bans.
     * API calls that require currency assets can be referenced using their ISO4217-A3 names in the case of ISO registered names, their 3 letter commonly used names in the case of unregistered names, or their X-ISO4217-A3 code (see http://www.ifex-project.org/).
     *
     * @return string of api sign value
     */
    private String calculateApiSignValue(String uriPath) {

        // Encrpyt Key = base64 decoded secret API key
        byte[] secretKey = getBase64DecodedSecretAPIKey();

        String nonce = Long.toUnsignedString(getNextNonce());
        final String postData = "";    // TODO: If 2 factor auth, put 2factor password here
        String nonceAndPostData = nonce+postData;

        String sha256NoncePostData = getSHA256HashValue(nonceAndPostData);
        String apiSignValue = uriPath+sha256NoncePostData;
        String encodedValue = getHMACSHA512ValueInBase64(apiSignValue, secretKey);

        return encodedValue;
    }

    /**
     * Returns the secret API key used in API-Sign value generation.
     * @return
     */
    private byte[] getBase64DecodedSecretAPIKey() {
        return Base64.getDecoder().decode(privateApiKey);
    }

    /**
     * Encrypts plaintext using HMAC SHA512 using key and returns in Base64 encoded string.
     *
     * https://stackoverflow.com/questions/39355241/compute-hmac-sha512-with-secret-key-in-java
     *
     * @param plaintext
     * @param keyBytes
     * @return
     */
    private String getHMACSHA512ValueInBase64(String plaintext, byte[] keyBytes) {
        String base64Result = null;
        try {
            Mac SHA512 = Mac.getInstance(HMAC_SHA512);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, HMAC_SHA512);

            SHA512.init(keySpec);

            byte[] macData = SHA512.doFinal(plaintext.getBytes(UTF8));
            base64Result = Base64.getEncoder().encodeToString(macData);
        } catch (UnsupportedEncodingException ex) {
            log.error("Unsupported encoding (using '"+UTF8+"'): "+ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            log.error("No such algorithm exception (using '"+HMAC_SHA512+"'): "+ex.getMessage());
        } catch (InvalidKeyException ex) {
            log.error("Invalid key exception: "+ex.getMessage());
        }

        return base64Result;
    }

    private String getSHA256HashValue(String plaintext) {
        byte[] encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256);
            encodedHash = digest.digest(plaintext.getBytes(UTF8));
        } catch (UnsupportedEncodingException ex) {
            log.error("Unsupported encoding (using '"+UTF8+"'): "+ex.getMessage());
        } catch (NoSuchAlgorithmException ex) {
            log.error("No such algorithm exception (using '"+SHA256+"'): "+ex.getMessage());
        }
        return Base64.getEncoder().encodeToString(encodedHash);
    }

    /**
     * Returns a new nonce value. The nonce is an always increasing unsigned 64 bit integer. This method uses a nonce
     * based on the current time plus a few milliseconds.
     *
     * Notes:
     * There is no way to reset this value once used. Too many requests using a nonce less than a preivous used value can
     * result in a ban.
     *
     * @return a new, usable nonce value
     */
    private long getNextNonce() {
        long unixTime = System.currentTimeMillis() / 1000L;

        return unixTime;
    }

}
