package com.diezel.cryptofeed.vendor.kraken;

import com.diezel.cryptofeed.vendor.kraken.model.KrakenEndpointNames;
import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenAssetInfo;
import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenResponse;
import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenServerTime;
import com.diezel.cryptofeed.vendor.kraken.service.KrakenHTTPHeaderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

/**
 * Client for interacting with the Kraken exchange API.
 *
 * @author dzale
 */
@Component
public class KrakenClient {

    private static final Logger log = LoggerFactory.getLogger(KrakenClient.class);

    // User Values
    @Value( "${cryptofeed.vendor.kraken.baseUrl:https://api.kraken.com}" )
    private String baseUrl;

    @Value( "${cryptofeed.vendor.kraken.apiVersion:0}")
    private String apiVersion;

    // Class Variables
    private RestTemplate restTemplate;

    @Autowired
    KrakenHTTPHeaderService httpHeaderGenerator;

    @Autowired
    ObjectMapper objectMapper;

    public KrakenClient() {
        restTemplate = new RestTemplate();
    }

    public long getServerUnixTime() {

        // Make Request
        String endpoint = getFullEndpointName(KrakenEndpointNames.SERVER_UNIXTIME);
        ResponseEntity<KrakenResponse> response =
                restTemplate.getForEntity(endpoint, KrakenResponse.class);
        KrakenResponse body = response.getBody();

        // Check Response For Error
        if (response.getStatusCode() != HttpStatus.OK) {
            // Error
            handleKrakenErrorResponse(response.getBody().getError());
            return 0;
        }

        // Otherwise Parse Response
        KrakenServerTime serverTime = null;
        try {
            serverTime = objectMapper.readValue((String)body.getResult(), KrakenServerTime.class);
        } catch (IOException ex) {
            log.error("Error reading GetServerTime response from API.", ex);
        }

        return serverTime == null ? 0 : serverTime.getUnixtime();
    }

    public KrakenAssetInfo[] getAssetInfo() {

        // Make Request
        String endpoint = getFullEndpointName(KrakenEndpointNames.ASSET_INFO);
        ResponseEntity<KrakenResponse> response =
                restTemplate.getForEntity(endpoint, KrakenResponse.class);
        KrakenResponse body = response.getBody();

        // Check Response For Error
        if (response.getStatusCode() != HttpStatus.OK) {
            // Error
            handleKrakenErrorResponse(response.getBody().getError());
            return null;
        }

        // Otherwise Parse Response
        KrakenAssetInfo[] assetInfo = null;
        try {
            assetInfo = objectMapper.readValue((String)body.getResult(), KrakenAssetInfo[].class);
        } catch (IOException ex) {
            log.error("Error reading GetServerTime response from API.", ex);
        }

        return assetInfo;
    }

    private String getFullEndpointName(KrakenEndpointNames name) {
        return baseUrl + name.toString();
    }

    private void handleKrakenErrorResponse(List<String> errors) {
        log.info("Kraken Errors:");
        int n = 1;
        for (String message : errors) {
            log.info("Error (#"+n+"): "+message);
            n++;
        }
    }

    public void callPrivateEndpoint(String endpointUri) {
        HttpHeaders headers = httpHeaderGenerator.getKrakenApiRequestHeaders(endpointUri);

        // TODO: Attach headers and make API call

    }


}
