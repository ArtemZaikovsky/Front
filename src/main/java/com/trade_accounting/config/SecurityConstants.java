package com.trade_accounting.config;

public class SecurityConstants {
    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME = 86400000;    // 24hrs
    // public static final long EXPIRATION_TIME = 900000;   // 15 min
    // public static final long EXPIRATION_TIME = 60000;    // 1 min
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHENTICATION_URL = "/login";
    public static final String SIGN_UP_URL = "/";

    public static final String TOKEN_ATTRIBUTE_NAME = "token";
}
