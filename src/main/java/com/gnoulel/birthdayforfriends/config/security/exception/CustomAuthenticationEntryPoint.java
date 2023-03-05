package com.gnoulel.birthdayforfriends.config.security.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gnoulel.birthdayforfriends.constants.AppConstant;
import com.gnoulel.birthdayforfriends.constants.SecurityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private String path;
    private String realmAuthSchema;
    private int status;
    private String customMessage;
    private String errorMessage;

    CustomAuthenticationEntryPoint() {
        setInitValues();
    }

    public void setInitValues() {
        this.path = SecurityConstant.SIGN_IN_URI_ENDING;
        this.realmAuthSchema = SecurityConstant.BASIC_TOKEN_PREFIX;
        this.status = HttpServletResponse.SC_UNAUTHORIZED;
        this.customMessage = "";
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        if (StringUtils.isBlank(customMessage)) {
            errorMessage = authException.getMessage();
        } else {
            errorMessage = customMessage + " - " + authException.getMessage();
        }
        response.setHeader(SecurityConstant.REALM_HEADER, this.realmAuthSchema + " realm=\"" + this.path + "\"");
        response.setStatus(status);
        response.setContentType(AppConstant.APPLICATION_JSON);
        response.setCharacterEncoding(AppConstant.ENCODING_UTF8);

        response.getWriter().write(getJsonString());
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public void setRealmAuthSchema(String realmAuthSchema) {
        this.realmAuthSchema = realmAuthSchema;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJsonString() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode errorObject = mapper.createObjectNode();
        errorObject.put(AppConstant.TIMESTAMP, ZonedDateTime.now().format(DateTimeFormatter.ofPattern(AppConstant.DATETIME_FORMAT)));
        errorObject.put(AppConstant.STATUS, status);
        errorObject.put(AppConstant.ERROR, HttpStatus.valueOf(status).getReasonPhrase());
        errorObject.put(AppConstant.MESSAGE, errorMessage);
        errorObject.put(AppConstant.PATH, path);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorObject);
    }
}
