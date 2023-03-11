package com.gnoulel.birthdayforfriends.exception;

public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -1777079037752957337L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
