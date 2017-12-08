package com.diezel.cryptofeed.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * A supporting Controller which attempts to catch any Exceptions not caught and handled inside other controller
 * methods. It will log and return an appropriate error response to the client.
 *
 * @author dzale
 */
@Controller
public class ErrorController {

    private static final Logger log = LoggerFactory.getLogger(ErrorController.class);

    /*
        Note on Default Exception Handler Priority:
            In the situation that an Exception is thrown and not handled, which matches multiple ExceptionHandlers,
            Spring will execute the "closest" possible handler, or child-most in the inheritance heirarchy. In this
            way, you may have generic error catching logic, and as you discover errors requiring different behavior,
            simply add a method with that more-specific exception and the custom behavior.
     */

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<String> handleException(Exception ex, WebRequest request) {
        log.error("Caught Generic Exception in Error Controller: ", ex);
        return new ResponseEntity<String>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*
       NOTE:
           By default when an invalid and/or poorly formatted request is sent to a controller endpoint, Spring
           will silently return a BAD_REQUEST response to client. By catching this we are at least aware the client made
           an attempt
    */
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleBadRequest(HttpMessageNotReadableException ex, WebRequest request) {
        log.trace("Received an invalid request from client.", ex.getMessage());
        return new ResponseEntity<String>("Invalid request.", HttpStatus.BAD_REQUEST);
    }

}
