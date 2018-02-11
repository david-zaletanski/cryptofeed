package com.diezel.cryptofeed.security.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A structure to hold the data used for user login / authentication.
 *
 * @author dzale
 */
@Data
@AllArgsConstructor
public class CryptofeedUserLogin {

    private final String username;

    private final String password;

}
