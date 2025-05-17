package org.yez.petbackend.service.token;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yez.petbackend.domain.token.Token;
import org.yez.petbackend.repository.token.TokenEntity;
import org.yez.petbackend.repository.token.TokenRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    public TokenServiceImpl(TokenRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Token createToken(UUID userId, String name, Instant expiresAt) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        TokenEntity tokenEntity = new TokenEntity();
        tokenEntity.setUser(user);
        tokenEntity.setName(name);
        tokenEntity.setToken(generateToken());
        tokenEntity.setExpiresAt(expiresAt);

        TokenEntity savedToken = tokenRepository.save(tokenEntity);
        return mapToToken(savedToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Token> getTokensByUserId(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return tokenRepository.findAllByUser(user).stream()
                .map(this::mapToToken)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        tokenRepository.deleteByToken(token);
    }

    @Override
    @Transactional
    public void deleteAllTokensByUserId(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        tokenRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UUID validateToken(String token) {
        return tokenRepository.findByToken(token)
                .filter(t -> !t.isExpired())
                .map(t -> t.getUser().getId())
                .orElse(null);
    }

    private Token mapToToken(TokenEntity entity) {
        return Token.builder()
                .id(entity.getId())
                .token(entity.getToken())
                .name(entity.getName())
                .userId(entity.getUser().getId())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
