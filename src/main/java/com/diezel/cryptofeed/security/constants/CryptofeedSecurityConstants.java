package com.diezel.cryptofeed.security.constants;

import java.util.concurrent.TimeUnit;

/**
 * Constants or loaded settings related to security.
 *
 * @author dzale
 */
public class CryptofeedSecurityConstants {

    public static final long MAX_TOKEN_LIFE_MILLISECONDS = TimeUnit.HOURS.toMillis(1); // 1 Hour to Milliseconds

    public static final String HTTP_AUTH_HEADER = "Authentication";
    public static final String HTTP_AUTH_HEADER_JWT_PREFIX = "Bearer ";

    public static final String JWT_CLAIMS_ROLES_KEY = "CFUserRoles";

    public static final String JWT_SIGNING_KEY = "ABCDefgh*$@&^!pqRsTuVWXYZ123-2-11-18";
    public static final String JWT_SIGNING_KEY_ENCODING = "UTF-8";

}
