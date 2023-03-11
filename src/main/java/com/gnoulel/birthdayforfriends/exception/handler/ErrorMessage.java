package com.gnoulel.birthdayforfriends.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class ErrorMessage {
    private int status;
    private String timestamp;
    private String error;
    private String message;
    private String path;

    public ErrorMessage(int status, String error, String message, String path) {
        this(
                status,
                ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
                error,
                message,
                path);
    }

    public ErrorMessage(int status, String timestamp, String error, String message, String path) {
        this.status = status;
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}
