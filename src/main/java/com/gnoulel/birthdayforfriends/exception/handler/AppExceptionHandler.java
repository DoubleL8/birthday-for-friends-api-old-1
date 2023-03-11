package com.gnoulel.birthdayforfriends.exception.handler;

import com.gnoulel.birthdayforfriends.exception.EmailExistedException;
import com.gnoulel.birthdayforfriends.exception.FriendNotFoundException;
import com.gnoulel.birthdayforfriends.exception.PhoneExistedException;
import com.gnoulel.birthdayforfriends.exception.UserExistedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class AppExceptionHandler {
    private static final String TAG = "AppExceptionHandler | ";

    @ExceptionHandler(UserExistedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage userExistedException(RuntimeException ex, HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler({EmailExistedException.class, PhoneExistedException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage emailOrPhoneExistedException(RuntimeException ex, HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(FriendNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage friendNotFoundException(FriendNotFoundException ex, HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage illegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleGenericException(Exception ex, HttpServletRequest request) {
        log.error(TAG + "Unexpected Error Happened!", ex);

        return new ErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
    }
}
