package com.diezel.cryptofeed.vendor.kraken.model.dto;

import lombok.Data;

/**
 * A storage DTO from Kraken asset information retrieval.
 *
 * @author dzale
 */
@Data
public class KrakenAssetInfo {

    private String asset_name;

    private String altname;

    private String aclass;

    private float decimals;

    private float display_decimals;

}
