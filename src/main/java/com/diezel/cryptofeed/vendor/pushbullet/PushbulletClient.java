package com.diezel.cryptofeed.vendor.pushbullet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Used to make service calls and interact with the Pushbullet API.
 *
 * @author dzale
 */
@Component
public class PushbulletClient {
    private static final Logger log = LoggerFactory.getLogger(PushbulletClient.class);

    // User Values
    @Value( "${cryptofeed.vendor.pushbullet.baseUrl}" )  // https://api.pushbullet.com
    private String baseUrl;

    // Class Variables
    private RestTemplate restTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public PushbulletClient() {
        this.restTemplate = new RestTemplate();
    }

}
