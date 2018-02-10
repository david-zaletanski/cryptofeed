package com.diezel.cryptofeed.vendor.kraken.model;

/**
 * Kraken API endpoint names as of 2/10/18.
 *
 * @author dzale
 */
public enum KrakenEndpointNames {

    // PUBLIC ENDPOINTS

    SERVER_UNIXTIME ( "/0/public/Time" ),
    ASSET_INFO ( "0/public/Assets" ),
    TRADABLE_ASSET_PAIRS ( "/0/public/AssetPairs" ),
    TICKER_INFORMATION ( "/0/public/Ticker" ),
    OHLC_DATA ( "/0/public/OHLC" ),
    ORDER_BOOK ( "/0/public/Depth" ),
    RECENT_TRADES ( "/0/public/Trades" ),
    RECENT_SPREAD_DATA ( "/0/public/Spread" );

    private final String text;

    KrakenEndpointNames(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
