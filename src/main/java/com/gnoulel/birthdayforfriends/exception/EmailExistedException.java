package com.gnoulel.birthdayforfriends.exception;

public class EmailExistedException extends RuntimeException{
    private static final long serialVersionUID = 871611522806015706L;

    public EmailExistedException(String message) {
        super(message);
    }
}
