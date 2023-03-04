package com.gnoulel.birthdayforfriends.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class TokenUtils {
    @Value("${bff.jwt.secret}")
    private String jwtSecret;

    @Value("${bff.jwt.expiration}")
    private long jwtExpirationMs;

    /**
     * Generate JWT Access Token
     * @param username
     * @return
     */
    public String generateJwtToken(String username) {

        Date currentDate = new Date();

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * Get username from JWT Token
     * @param token
     * @return
     */
    public String getUsernameFromJwtToken(String token) {
        Jws<Claims> claims =  Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token);

        return claims.getBody().getSubject();
    }

    /**
     * Check if JWT token is valid or not
     * @param token
     * @return
     */
    public boolean isJwtTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parse(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
