package com.ironhack.torneo_germans_bisbal_api.service;

import com.ironhack.torneo_germans_bisbal_api.model.User;

/**
 * The RefreshTokenService is an interface that defines the methods that are available to
 * issue, validate/rotate and revoke refresh tokens.
 */
public interface RefreshTokenService {

    /**
     * Issues a new refresh token for the given user, persisting only its hash.
     *
     * @param user the user to issue the refresh token for.
     * @return the raw refresh token (to be sent to the client), never the hash.
     */
    String issue(User user);

    /**
     * Validates a raw refresh token and rotates it: the token used is revoked and a new
     * one is issued for the same user.
     *
     * @param rawToken the raw refresh token received from the client.
     * @return the user the token belongs to and the new raw refresh token.
     */
    RotationResult validateAndRotate(String rawToken);

    /**
     * Revokes a raw refresh token. Does nothing if the token does not exist.
     *
     * @param rawToken the raw refresh token to revoke.
     */
    void revoke(String rawToken);

    record RotationResult(User user, String rawToken) {
    }
}
