package com.diezel.cryptofeed.vendor.kraken.model.dto;

import lombok.Data;

/**
 * Created by zalet on 2/10/2018.
 */
@Data
public class KrakenServerTime {

    private long unixtime;

    private String rfc1123;

}
