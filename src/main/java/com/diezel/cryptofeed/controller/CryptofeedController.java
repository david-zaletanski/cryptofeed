package com.diezel.cryptofeed.controller;

import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.service.CryptofeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The main Spring Controller for this application.
 *
 * @author dzale
 */
@RestController(value = "/crypto")
public class CryptofeedController {

    private static final Logger log = LoggerFactory.getLogger(CryptofeedController.class);

    @Autowired
    CryptofeedService cryptofeedService;

    /**
     * Returns a friendly greeting.
     *
     * @param world optionally specify who to say hello to, the world by default
     * @return a friendly hello world String
     */
    @RequestMapping(value = "/hello/{world}", method = RequestMethod.GET)
    public ResponseEntity<String> hello(@PathVariable(name = "world", required = false) String world) {
        String response = cryptofeedService.hello(world);
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addUser(CryptofeedUser user) {
        boolean success = cryptofeedService.addUser(user);
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

}
