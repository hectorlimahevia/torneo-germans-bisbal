package com.ironhack.torneo_germans_bisbal_api.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Generates access tokens (JWT), so that login and refresh always build them the exact same way.
 */
@Component
public class JwtService {

    private final String jwtSecret;
    private final long accessTokenExpirationMinutes;

    public JwtService(@Value("${app.jwt.secret}") String jwtSecret,
                       @Value("${app.jwt.access-token-expiration-minutes:15}") long accessTokenExpirationMinutes) {
        this.jwtSecret = jwtSecret;
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes;
    }

    public String generateAccessToken(String username, List<String> roles, String issuer) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret.getBytes());

        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenExpirationMinutes * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("roles", roles)
                .sign(algorithm);
    }
}
