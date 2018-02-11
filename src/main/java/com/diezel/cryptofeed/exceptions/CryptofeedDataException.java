package com.diezel.cryptofeed.exceptions;

/**
 * Exceptions related to data errors.
 *
 * @author dzale
 */
public class CryptofeedDataException extends CryptofeedException {

    public CryptofeedDataException(String message) {
        super(message);
    }

    public CryptofeedDataException(String message, Throwable cause) {
        super(message, cause);
    }

}
