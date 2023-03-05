package com.gnoulel.birthdayforfriends.config.security.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gnoulel.birthdayforfriends.constants.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setContentType(AppConstant.APPLICATION_JSON);
        int status = HttpServletResponse.SC_FORBIDDEN;
        String path = request.getRequestURI();
        String message = "Insufficient Privileges - " + accessDeniedException.getMessage();

        response.setStatus(status);
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put(AppConstant.TIMESTAMP, LocalDateTime.now().format(DateTimeFormatter.ofPattern(AppConstant.DATETIME_FORMAT)));
        objectNode.put(AppConstant.STATUS, status);
        objectNode.put(AppConstant.ERROR, HttpStatus.valueOf(status).getReasonPhrase());
        objectNode.put(AppConstant.MESSAGE, message);
        objectNode.put(AppConstant.PATH, path);

        mapper.writerWithDefaultPrettyPrinter().writeValue(response.getOutputStream(), objectNode);
    }
}
