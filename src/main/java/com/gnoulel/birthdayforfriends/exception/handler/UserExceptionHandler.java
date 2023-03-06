package com.gnoulel.birthdayforfriends.exception.handler;

import com.gnoulel.birthdayforfriends.exception.ErrorMessage;
import com.gnoulel.birthdayforfriends.exception.UserExistedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * The @RestControllerAdvice annotation is specialization of @Component annotation
 * so that it is auto-detected via classpath scanning.
 * It is a kind of interceptor that surrounds the logic in our Controllers
 * and allows us to apply some common logic to them.
 *
 * Rest Controller Advice’s methods (annotated with @ExceptionHandler) are shared globally
 * across multiple @Controller components to capture exceptions and translate them to HTTP responses.
 * The @ExceptionHandler annotation indicates which type of Exception we want to handle.
 * The exception instance and the request will be injected via method arguments.
 *
 * By using two annotations together, we can:
 * 1. control the body of the response along with status code
 * 2. handle several exceptions in the same method
 */
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class UserExceptionHandler {

    @ExceptionHandler({UserExistedException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage userExistedException(RuntimeException ex, HttpServletRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI());
    }

}
