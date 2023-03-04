package com.gnoulel.birthdayforfriends.config;

import com.gnoulel.birthdayforfriends.config.filters.CustomRequestHeaderTokenFilter;
import com.gnoulel.birthdayforfriends.config.userdetails.CustomUserDetailsService;
import com.gnoulel.birthdayforfriends.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = false,
        jsr250Enabled = false,
        prePostEnabled = true)
public class SecurityConfig {
    @Value("${api.auth.signin.url}")
    private String signInUrl;
    @Value("${api.auth.signup.url}")
    private String signUpUrl;

    private final AuthenticationConfiguration authenticationConfiguration;
    private final CustomUserDetailsService userDetailsService;
    private final TokenUtils tokenUtils;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration,
                          CustomUserDetailsService userDetailsService,
                          TokenUtils tokenUtils) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    public CustomRequestHeaderTokenFilter customRequestHeaderTokenFilter() throws Exception {
        CustomRequestHeaderTokenFilter customFilter =
                new CustomRequestHeaderTokenFilter(authenticationConfiguration.getAuthenticationManager(),
                        tokenUtils, userDetailsService);
        customFilter.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(signInUrl, "POST"));

        return customFilter;
    }

    /**
     * Configuration of a SecurityFilterChain bean, actually means
     * adding (or removing) one or more filters from the chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .antMatchers( HttpMethod.POST,"/api/auth/signup").permitAll()
                .antMatchers("/api/friends").permitAll()
                .anyRequest().authenticated();

        http.authenticationProvider(daoAuthenticationProvider());
        http.addFilter(customRequestHeaderTokenFilter());

        return http.build();
    }
}
