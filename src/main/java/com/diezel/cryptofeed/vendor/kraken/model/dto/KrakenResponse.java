package com.diezel.cryptofeed.vendor.kraken.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Represents the error response sent by Kraken API.
 *
 * @author dzale
 */
@Data
@Builder
@AllArgsConstructor
public class KrakenResponse {

    private List<String> error;

    private Object result;

}
