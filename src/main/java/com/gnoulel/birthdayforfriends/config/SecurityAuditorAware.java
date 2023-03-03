package com.gnoulel.birthdayforfriends.config;

import com.gnoulel.birthdayforfriends.entity.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * In case you use either @CreatedBy or @LastModifiedBy, the auditing
 * infrastructure somehow needs to become aware of the current principal.
 * To do so, we provide an AuditorAware<T> SPI interface that you have to
 * implement to tell the infrastructure who the current user or system
 * interacting with the application is. The generic type T defines what
 * type the properties annotated with @CreatedBy or @LastModifiedBy have to be.
 */
@Component
public class SecurityAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getCredentials)
                .map(String.class::cast);
    }
}
