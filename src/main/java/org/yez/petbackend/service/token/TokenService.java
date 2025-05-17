package org.yez.petbackend.service.token;

import org.yez.petbackend.domain.token.Token;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TokenService {
    /**
     * Create a new token for a user
     * @param userId the ID of the user
     * @param name the name of the token
     * @param expiresAt the expiration time of the token, null for never expire
     * @return the created token
     */
    Token createToken(UUID userId, String name, Instant expiresAt);
    
    /**
     * Get all tokens for a user
     * @param userId the ID of the user
     * @return a list of tokens
     */
    List<Token> getTokensByUserId(UUID userId);
    
    /**
     * Delete a token
     * @param token the token to delete
     */
    void deleteToken(String token);
    
    /**
     * Delete all tokens for a user
     * @param userId the ID of the user
     */
    void deleteAllTokensByUserId(UUID userId);
    
    /**
     * Validate a token and return the user ID if valid
     * @param token the token to validate
     * @return the user ID if the token is valid, null otherwise
     */
    UUID validateToken(String token);
}
