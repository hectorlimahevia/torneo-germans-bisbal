package com.ironhack.torneo_germans_bisbal_api.service.impl;

import com.ironhack.torneo_germans_bisbal_api.exception.InvalidRefreshTokenException;
import com.ironhack.torneo_germans_bisbal_api.model.RefreshToken;
import com.ironhack.torneo_germans_bisbal_api.model.User;
import com.ironhack.torneo_germans_bisbal_api.repository.RefreshTokenRepository;
import com.ironhack.torneo_germans_bisbal_api.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.refresh-token.expiration-days:30}")
    private long refreshTokenExpirationDays;

    @Override
    @Transactional
    public String issue(User user) {
        byte[] randomBytes = new byte[48];
        SECURE_RANDOM.nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hash(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plus(Duration.ofDays(refreshTokenExpirationDays)));
        refreshToken.setRevoked(false);
        refreshToken.setCreatedAt(Instant.now());

        refreshTokenRepository.save(refreshToken);
        log.info("Issued new refresh token for user {}", user.getUsername());

        return rawToken;
    }

    @Override
    @Transactional
    public RotationResult validateAndRotate(String rawToken) {
        Optional<RefreshToken> found = refreshTokenRepository.findByTokenHash(hash(rawToken));

        RefreshToken refreshToken = found.orElseThrow(
                () -> new InvalidRefreshTokenException("Invalid refresh token"));

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token has been revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidRefreshTokenException("Refresh token has expired");
        }

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);

        // Force-initialize the lazy User proxy now, while the session is still open, so it
        // stays usable (e.g. getUsername(), getRoles()) after this method returns.
        User user = refreshToken.getUser();
        user.getUsername();

        String newRawToken = issue(user);

        return new RotationResult(user, newRawToken);
    }

    @Override
    @Transactional
    public void revoke(String rawToken) {
        refreshTokenRepository.findByTokenHash(hash(rawToken)).ifPresent(refreshToken -> {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        });
    }

    private String hash(String rawToken) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawToken.getBytes());
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
