package com.gnoulel.birthdayforfriends.exception;

public class FriendNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -5169250810388479694L;

    public FriendNotFoundException(String message) {
        super(message);
    }
}
