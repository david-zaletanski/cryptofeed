package com.diezel.cryptofeed.controller;

import com.diezel.cryptofeed.exceptions.CryptofeedDataException;
import com.diezel.cryptofeed.exceptions.CryptofeedException;
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
    public ResponseEntity<Boolean> addUser(CryptofeedUser user) throws CryptofeedException {
        boolean success = cryptofeedService.addUser(user);
        if (!success)
            throw new CryptofeedException("Unabled to add user '"+user.getUsername()+"'.");
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @RequestMapping(value = "/users/{userId}",
            method = RequestMethod.GET,
            produces = { "application/json" } )
    public ResponseEntity<CryptofeedUser> getUser(Long userId) throws CryptofeedException {
        CryptofeedUser user = cryptofeedService.getUser(userId);
        if (user == null)
            throw new CryptofeedDataException("User with userId '"+userId+"' not found.");
        return new ResponseEntity<CryptofeedUser>(user, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/users?username=",
            method = RequestMethod.GET,
            produces = { "application/json" } )
    public ResponseEntity<CryptofeedUser> getUser(String username) throws CryptofeedException {
        CryptofeedUser user = cryptofeedService.getUserByUsername(username);
        if (user == null)
            throw new CryptofeedDataException("User with username '"+username+"' not found.");
        return new ResponseEntity<CryptofeedUser>(user, HttpStatus.OK);
    }

}
