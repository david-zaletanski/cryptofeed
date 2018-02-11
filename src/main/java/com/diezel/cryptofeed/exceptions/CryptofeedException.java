package com.diezel.cryptofeed.exceptions;

/**
 * A generic exception encapsulator.
 *
 * @author dzale
 */
public class CryptofeedException extends Exception {

    public CryptofeedException(String message) {
        super(message);
    }

    public CryptofeedException(String message, Throwable cause) {
        super(message, cause);
    }

}
