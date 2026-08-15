package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.User;

public interface RefreshTokenService {

    String issue(User user);

    RotationResult validateAndRotate(String rawToken);

    void revoke(String rawToken);

    record RotationResult(User user, String rawToken) {
    }
}
