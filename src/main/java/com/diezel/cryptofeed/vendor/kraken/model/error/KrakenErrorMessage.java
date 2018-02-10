package com.diezel.cryptofeed.vendor.kraken.model.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Format of error message within the error Kraken response:
 *
 * <char-severity code><string-error category>:<string-error type>[:<string-extra info>]
 *      severity code can be E for error or W for warning
 *
 * @author dzale
 */
@Data
@Builder
@AllArgsConstructor
public class KrakenErrorMessage {

    private char severityCode;

    private String errorCategory;

    private String errorType;

    private String extraInfo;

    private String fullMessageText;

    /**
     * Breaks down Kraken error message in to component parts.
     *
     * @param message full length message
     */
    public KrakenErrorMessage(String message) {
        this.fullMessageText = message;

        // TODO: Breakdown in to component parts.
    }

    private String getStringFromParts() {
        StringBuilder sb = new StringBuilder();
        sb.append(severityCode);
        sb.append(errorCategory);
        sb.append(":");
        sb.append(errorType);
        sb.append("[:"+extraInfo+"]");
        return sb.toString();
    }

    @Override
    public String toString() {

        return fullMessageText;
    }

}
