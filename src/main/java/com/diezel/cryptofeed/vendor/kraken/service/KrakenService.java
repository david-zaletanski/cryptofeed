package com.diezel.cryptofeed.vendor.kraken.service;

import com.diezel.cryptofeed.vendor.kraken.KrakenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Managages logic of interactions with Kraken API.
 *
 * @author dzale
 */
@Service
public class KrakenService {

    @Autowired
    KrakenClient krakenApi;

    public long getServerTime() {
        long krakenServerTime = krakenApi.getServerUnixTime();

        return krakenServerTime;
    }

}
