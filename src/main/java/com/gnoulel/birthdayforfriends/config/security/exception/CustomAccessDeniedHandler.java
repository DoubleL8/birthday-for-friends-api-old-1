package com.gnoulel.birthdayforfriends.config.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnoulel.birthdayforfriends.constants.AppConstant;
import com.gnoulel.birthdayforfriends.exception.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        int status = HttpServletResponse.SC_FORBIDDEN;
        String path = request.getRequestURI();
        String message = "Insufficient Privileges - " + accessDeniedException.getMessage();

        response.setContentType(AppConstant.APPLICATION_JSON);
        response.setStatus(status);

        ErrorMessage errorMessage = new ErrorMessage(
                status,
                HttpStatus.valueOf(status).getReasonPhrase(),
                message,
                path
        );
        new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), errorMessage);
    }
}
