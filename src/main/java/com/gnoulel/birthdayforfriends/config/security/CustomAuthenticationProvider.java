package com.gnoulel.birthdayforfriends.config.security;

import com.gnoulel.birthdayforfriends.config.security.userdetails.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken;

        if (Objects.isNull(authentication)) {
            return null;
        }

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(username);

        if (Objects.isNull(user)) throw new UsernameNotFoundException("Username not found!");

        if (passwordEncoder.matches(password, user.getPassword())) {
            authToken = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Bad Credentials");
        }

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
