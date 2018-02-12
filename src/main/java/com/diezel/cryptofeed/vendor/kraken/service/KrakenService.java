package com.diezel.cryptofeed.vendor.kraken.service;

import com.diezel.cryptofeed.vendor.kraken.KrakenClient;
import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenAssetInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

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

    public List<KrakenAssetInfo> getAssetInfo() {
        KrakenAssetInfo[] assetInfo = krakenApi.getAssetInfo();

        return Arrays.asList(assetInfo);
    }

}
