package com.gnoulel.birthdayforfriends.config.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnoulel.birthdayforfriends.config.userdetails.CustomUserDetails;
import com.gnoulel.birthdayforfriends.constants.AppConstant;
import com.gnoulel.birthdayforfriends.constants.SecurityConstant;
import com.gnoulel.birthdayforfriends.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class CustomRequestHeaderTokenFilter extends UsernamePasswordAuthenticationFilter {
    private static final String TAG = "CustomRequestHeaderTokenFilter | ";

    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private final UserDetailsService userDetailsService;

    public CustomRequestHeaderTokenFilter(AuthenticationManager authenticationManager,
                                          TokenUtils tokenUtils,
                                          UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*
         * Invokes the requiresAuthentication() method to determine whether the request is for authentication and should be handled
         * by this filter. If it is an authentication request, the attemptAuthentication() will be invoked to perform the authentication.
         * There are then three possible outcomes:
             1.	An Authentication object is returned. The configured SessionAuthenticationStrategy will be invoked (to handle any
                 session-related behavior such as creating a new session to protect against session-fixation attacks) followed by the
                 invocation of successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication) method
             2.	An AuthenticationException occurs during authentication. The unsuccessfulAuthentication method will be invoked
             3.	Null is returned, indicating that the authentication process is incomplete. The method will then return immediately,
                assuming that the subclass has done any necessary work (such as redirects) to continue the authentication process. The assumption is that a later request will be received by this method where the returned Authentication object is not null.
         */
        log.info(TAG + "DO_FILTER | ... Is Authentication required?");

        HttpServletRequest httpReq = (HttpServletRequest) request;
        String username = getUsernameFromBearerToken(httpReq.getHeader(SecurityConstant.AUTH_HEADER));
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (!Objects.isNull(user)) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            log.info(TAG + "Authorized Successfully!");
        }

        super.doFilter(request, response, chain);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

       /*
           Performs actual authentication.
           The implementation should do one of the following:
           1. Return a populated authentication token for the authenticated user, indicating successful authentication
           2. Return null, indicating that the authentication process is still in progress.
              Before returning, the implementation should perform any additional work required to complete the process.
           3. Throw an AuthenticationException if the authentication process fails
           So, it should return either the authenticated user token, or null if authentication is incomplete.
        */

        log.info(TAG + "Authentication Processing ... | Attempting Authentication ... ");

        UsernamePasswordAuthenticationToken unauthenticatedToken =
                getUnauthenticatedToken(request.getHeader(SecurityConstant.AUTH_HEADER));

        return authenticationManager.authenticate(unauthenticatedToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

       /*
           Default behavior for successful authentication:
           - Sets the successful Authentication object on the SecurityContextHolder
           - Informs the configured RememberMeServices of the successful login
           - Fires an InteractiveAuthenticationSuccessEvent via the configured ApplicationEventPublisher
           - Delegates additional behavior to the AuthenticationSuccessHandler.
           Subclasses can override this method to continue the FilterChain after successful authentication.
        */

        log.info(TAG + "Authentication Processing ... | SUCCESSFUL Authentication!");

        // Get principal from authentication
        CustomUserDetails userDetails = (CustomUserDetails) authResult.getPrincipal();
        // Get issuer -> the request URL
        String issuer = request.getRequestURL().toString();
        // Get role from principal authorities
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();

        String accessToken = tokenUtils.generateJwtToken(userDetails.getUsername());
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);

        // Return tokens to client
        response.setHeader(AppConstant.CONTENT_TYPE, AppConstant.APPLICATION_JSON);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    // @Override
    // protected void  unsuccessfulAuthentication(jakarta.servlet.http.HttpServletRequest request,
    //                                            jakarta.servlet.http.HttpServletResponse response,
    //                                            AuthenticationException failed)
    //                                            throws IOException, jakarta.servlet.ServletException {

    //     /*
    //         Default behaviour for unsuccessful authentication:
    //         Clears the SecurityContextHolder
    //         Stores the exception in the session (if it exists or allowSessionCreation is set to true)
    //         Informs the configured RememberMeServices of the failed login
    //         Delegates additional behaviour to the AuthenticationFailureHandler.

    //         Handle here an unsuccessful authentication, so you can set necessary response headers,
    //         content-type, or set the response status code and even  modify/add some JSON body to
    //         the response send back to the client
    //     */

    //     logger.info("==>> UN-SUCCESSFUL Authentication! " + failed.getMessage());

    //     // Here we can respond with ERROR Messages. For instance:

    // }

    /**
     * Get unauthenticated user credentials information
     * @param headerToken Basic Authorization token
     * @return User credentials information
     */
    private UsernamePasswordAuthenticationToken getUnauthenticatedToken(String headerToken) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", "");
        if (!isAuthorizationHeader(headerToken, SecurityConstant.BASIC_TOKEN_PREFIX)) {
            return authenticationToken;
        }

        String token = StringUtils.removeStart(headerToken, SecurityConstant.BASIC_TOKEN_PREFIX).trim();

        try {
            String[] credentials = decodeTokenBase64(token);
            String username = credentials[0];
            String password = credentials[1];

            authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        } catch (IllegalArgumentException e) {
            log.info(TAG + "Authentication Processing ... | Error getting the Basic authorization header! | " + e.getMessage());
        }

        return authenticationToken;
    }

    /**
     * Check the prefix of the Authorization header
     * @param headerToken Authorization header
     * @param prefix Token's prefix
     * @return True - If the Authorization header matches prefix
     */
    private boolean isAuthorizationHeader(String headerToken, String prefix) {
        return !StringUtils.isBlank(headerToken) && StringUtils.startsWith(headerToken, prefix);
    }

    /**
     * Decode credentials from base64 token
     * @param token a string to decode
     * @return 2-Item array of string contains username and password
     */
    private String[] decodeTokenBase64(String token) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.getDecoder().decode(token);
        String decodedToken = new String(decodedBytes);

        return decodedToken.split(":", 2);
    }

    /**
     * Get username from Bearer token
     * @param headerToken Bearer token
     * @return Username
     */
    private String getUsernameFromBearerToken(String headerToken) {
        if (!isAuthorizationHeader(headerToken, SecurityConstant.BEARER_TOKEN_PREFIX)) {
            log.info(TAG + "Authorization Header for Bearer (JWT) Authorization not provided or is null or invalid!");
            return "";
        }

        try {
            String token = StringUtils.removeStart(headerToken, SecurityConstant.BEARER_TOKEN_PREFIX).trim();
            String username = tokenUtils.getUsernameFromJwtToken(token);

            if (!Objects.isNull(username)) return username;
        } catch (Exception e) {
            log.info(TAG + "Error getting the Username from Bearer JWT token! - " + e.getMessage());
        }

        return "";
    }
}
