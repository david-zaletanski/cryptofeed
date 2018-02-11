package com.diezel.cryptofeed.vendor.kraken;

import com.diezel.cryptofeed.vendor.kraken.model.KrakenEndpointNames;
import com.diezel.cryptofeed.vendor.kraken.model.dto.KrakenServerTime;
import com.diezel.cryptofeed.vendor.kraken.model.error.KrakenErrorResponse;
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
    KrakenHTTPHeader httpHeaderGenerator;

    @Autowired
    ObjectMapper objectMapper;

    public KrakenClient() {
        restTemplate = new RestTemplate();
    }

    public long getServerUnixTime() {

        // Make Request
        String endpoint = getFullEndpointName(KrakenEndpointNames.SERVER_UNIXTIME);
        ResponseEntity<String> response =
                restTemplate.getForEntity(endpoint, String.class);
        String body = response.getBody();

        // Check Response For Error
        if (response.getStatusCode() != HttpStatus.OK) {
            // Error
            try {
                KrakenErrorResponse errorResponse = objectMapper.readValue(body, KrakenErrorResponse.class);
                handleKrakenErrorResponse(errorResponse);
                return 0;
            } catch (IOException ex) {
                log.error("Error converting API response body to KrakenErrorResponse. ", ex.getMessage());
            }
        }

        // Otherwise Parse Response
        KrakenServerTime serverTime = null;
        try {
            serverTime = objectMapper.readValue(body, KrakenServerTime.class);
        } catch (IOException ex) {
            log.error("Error reading GetServerTime response from API.", ex);
        }

        return serverTime == null ? 0 : serverTime.getUnixtime();
    }

    private String getFullEndpointName(KrakenEndpointNames name) {
        return baseUrl + name.toString();
    }

    private void handleKrakenErrorResponse(KrakenErrorResponse errorResponse) {
        log.info("Kraken Result: "+errorResponse.getResult());
        log.info("Kraken Errors:");
        int n = 1;
        for (String message : errorResponse.getError()) {
            log.info("Error (#"+n+"): "+message);
            n++;
        }
    }

    public void callPrivateEndpoint(String endpointUri) {
        HttpHeaders headers = httpHeaderGenerator.getKrakenApiRequestHeaders(endpointUri);

        // TODO: Attach headers and make API call

    }


}
