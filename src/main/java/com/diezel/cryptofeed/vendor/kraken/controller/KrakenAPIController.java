package com.diezel.cryptofeed.vendor.kraken.controller;

import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenAssetInfo;
import com.diezel.cryptofeed.vendor.kraken.service.KrakenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Send Kraken API calls through the cryptofeed interface.
 *
 * @author dzale
 */
@RestController
@RequestMapping( value = "/vendor/kraken" )
public class KrakenAPIController {

    private static final Logger log = LoggerFactory.getLogger(KrakenAPIController.class);

    @Autowired
    KrakenService krakenService;

    @RequestMapping(value = "/serverTime", method = RequestMethod.GET)
    public ResponseEntity<Long> getServerTime(HttpServletRequest request) {

        log.info("Received a request for Kraken server time.");
        long krakenServerTime = krakenService.getServerTime();

        return new ResponseEntity<Long>(new Long(krakenServerTime), HttpStatus.OK);
    }

    @RequestMapping(value = "/assetInfo", method = RequestMethod.GET)
    public ResponseEntity<List<KrakenAssetInfo>> getAssetInfo(HttpServletRequest request) {

        log.info("Received a request for Kraken asset info.");
        List<KrakenAssetInfo> assetInfo = krakenService.getAssetInfo();

        return new ResponseEntity<List<KrakenAssetInfo>>(assetInfo, HttpStatus.OK);
    }

}
