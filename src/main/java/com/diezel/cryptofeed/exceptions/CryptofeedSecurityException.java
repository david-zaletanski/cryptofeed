package com.diezel.cryptofeed.exceptions;

/**
 * A wrapper for security related exceptions.
 *
 * @author dzale
 */
public class CryptofeedSecurityException extends CryptofeedException {

    public CryptofeedSecurityException(String message) {
        super(message);
    }

    public CryptofeedSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

}
