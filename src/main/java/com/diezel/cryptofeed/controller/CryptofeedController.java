package com.diezel.cryptofeed.controller;

import com.diezel.cryptofeed.exceptions.CryptofeedDataException;
import com.diezel.cryptofeed.exceptions.CryptofeedException;
import com.diezel.cryptofeed.model.CryptofeedUser;
import com.diezel.cryptofeed.model.CryptofeedViews;
import com.diezel.cryptofeed.service.CryptofeedService;
import com.fasterxml.jackson.annotation.JsonView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * The main Spring Controller for this application.
 *
 * @author dzale
 */
@RestController
@RequestMapping( value = "/crypto" )
public class CryptofeedController {

    private static final Logger log = LoggerFactory.getLogger(CryptofeedController.class);

    @Autowired
    CryptofeedService cryptofeedService;

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addUser(@RequestBody CryptofeedUser user) throws CryptofeedException {
        boolean success = cryptofeedService.addUser(user);
        if (!success)
            throw new CryptofeedException("Unabled to add user '"+user.getUsername()+"'.");
        return new ResponseEntity<Boolean>(success, HttpStatus.OK);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<List<CryptofeedUser>> getUsers() throws CryptofeedException {
        List<CryptofeedUser> users = cryptofeedService.getUsers();

        return new ResponseEntity<List<CryptofeedUser>>(users, HttpStatus.OK);
    }

    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @RequestMapping(value = "/users/{userId}",
            method = RequestMethod.GET,
            produces = { "application/json" } )
    public ResponseEntity<CryptofeedUser> getUser(@RequestParam Long userId) throws CryptofeedException {
        CryptofeedUser user = cryptofeedService.getUser(userId);
        if (user == null)
            throw new CryptofeedDataException("Unable to find user with userId '"+userId+"'.");
        return new ResponseEntity<CryptofeedUser>(user, HttpStatus.OK);
    }

    @JsonView( value = { CryptofeedViews.IncludeInResponse.class } )
    @RequestMapping(
            value = "/users?username=",
            method = RequestMethod.GET,
            produces = { "application/json" } )
    public ResponseEntity<CryptofeedUser> getUserByUsername(String username) throws CryptofeedException {
        CryptofeedUser user = cryptofeedService.getUserByUsername(username);
        if (user == null)
            throw new CryptofeedDataException("Unable to find user with username '"+username+"'.");
        return new ResponseEntity<CryptofeedUser>(user, HttpStatus.OK);
    }

}
