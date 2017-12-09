package com.diezel.cryptofeed.service;

import org.springframework.stereotype.Service;

/**
 * The service supporting the main Spring Controller.
 *
 * @author dzale
 */
@Service
public class CryptofeedService {

    /**
     * Returns a hello world message.
     *
     * @param world who to say hello to. if null or empty, its the world
     * @return a hello world message
     */
    public String hello(String world) {
        return "Hello " + (world == null || world.isEmpty() ? "world!" : world);
    }

}
