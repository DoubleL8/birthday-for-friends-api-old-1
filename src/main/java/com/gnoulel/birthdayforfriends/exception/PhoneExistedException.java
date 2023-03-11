package com.gnoulel.birthdayforfriends.exception;

public class PhoneExistedException extends RuntimeException{
    private static final long serialVersionUID = 4855246687574002900L;

    public PhoneExistedException(String message) {
        super(message);
    }
}
