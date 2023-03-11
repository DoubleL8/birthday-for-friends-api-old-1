package com.gnoulel.birthdayforfriends.exception;

public class UserExistedException extends RuntimeException {
    private static final long serialVersionUID = 3180098876497076322L;

    public UserExistedException(String message) {
        super(message);
    }
}
