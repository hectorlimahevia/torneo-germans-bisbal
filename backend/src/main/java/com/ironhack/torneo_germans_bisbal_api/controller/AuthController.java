package com.ironhack.torneo_germans_bisbal_api.controller;

import com.ironhack.torneo_germans_bisbal_api.exception.InvalidRefreshTokenException;
import com.ironhack.torneo_germans_bisbal_api.model.Role;
import com.ironhack.torneo_germans_bisbal_api.model.User;
import com.ironhack.torneo_germans_bisbal_api.security.JwtService;
import com.ironhack.torneo_germans_bisbal_api.service.RefreshTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenService refreshTokenService;

    private final JwtService jwtService;

    @Value("${app.cookie.secure:true}")
    private boolean cookieSecure;

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                      HttpServletRequest request, HttpServletResponse response) {
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(Map.of("error_message", "No refresh token"));
        }

        RefreshTokenService.RotationResult rotationResult;
        try {
            rotationResult = refreshTokenService.validateAndRotate(refreshToken);
        } catch (InvalidRefreshTokenException exception) {
            return ResponseEntity.status(401).body(Map.of("error_message", exception.getMessage()));
        }

        User appUser = rotationResult.user();

        String accessToken = jwtService.generateAccessToken(
                appUser.getUsername(),
                appUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()),
                request.getRequestURL().toString());

        writeRefreshTokenCookie(response, rotationResult.rawToken(), 60 * 60 * 24 * 30);

        return ResponseEntity.ok(Map.of("access_token", accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse response) {
        if (refreshToken != null) {
            refreshTokenService.revoke(refreshToken);
        }

        writeRefreshTokenCookie(response, "", 0);

        return ResponseEntity.ok().build();
    }

    private void writeRefreshTokenCookie(HttpServletResponse response, String value, int maxAgeSeconds) {
        Cookie cookie = new Cookie("refresh_token", value);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/api");
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setAttribute("SameSite", "Lax");
        response.addCookie(cookie);
    }
}
