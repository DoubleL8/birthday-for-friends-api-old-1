package com.gnoulel.birthdayforfriends.constants;

public class SecurityConstant {
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";
    public static final String BASIC_TOKEN_PREFIX = "Basic ";
    public static final String SIGN_IN_URI_ENDING = "/signin";
    public static final String REALM_HEADER = "WWW-Authenticate";

    public static final String BEARER_TOKEN_INVALID_ERROR = "Authorization Header for Bearer (JWT) Authorization not provided or is null or invalid!";
    public static final String BEARER_TOKEN_USERNAME_ERROR = "Error getting the Username from Bearer JWT token! - {0}";
    public static final String BASIC_TOKEN_INVALID_ERROR = "Authorization Header for Basic Authentication not provided or is null or invalid";
    public static final String AUTH_HEADER_ERROR = "Error getting the Authorization Header - {0}";

    public static final String CREDENTIALS_NOT_PROVIDED_ERROR = "Username or password not provided!";
    public static final String CREDENTIALS_INCORRECT_ERROR = "Username or password incorrect!";
    public static final String USERNAME_NOT_FOUND_ERROR = "Username not found!";
}
