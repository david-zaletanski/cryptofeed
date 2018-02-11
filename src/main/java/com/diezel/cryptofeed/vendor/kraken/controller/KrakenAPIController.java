package com.diezel.cryptofeed.vendor.kraken.controller;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.vendor.kraken.service.KrakenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Send Kraken API calls through the cryptofeed interface.
 *
 * @author dzale
 */
@RestController
@RequestMapping( name = "/vendor/kraken" )
public class KrakenAPIController {

    @Autowired
    KrakenService krakenService;

    @RequestMapping(value = "/serverTime", method = RequestMethod.GET)
    public ResponseEntity<Long> getServerTime() {

        long krakenServerTime = krakenService.getServerTime();

        return new ResponseEntity<Long>(new Long(krakenServerTime), HttpStatus.OK);
    }

}
