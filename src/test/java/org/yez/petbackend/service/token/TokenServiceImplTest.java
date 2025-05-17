package org.yez.petbackend.service.token;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yez.petbackend.domain.token.Token;
import org.yez.petbackend.repository.token.TokenEntity;
import org.yez.petbackend.repository.token.TokenRepository;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TokenServiceImpl tokenService;

    private final UUID TEST_USER_ID = UUID.randomUUID();
    private final UUID TEST_TOKEN_ID = UUID.randomUUID();
    private final String TEST_TOKEN_STRING = "test-token-string";
    private final String TEST_TOKEN_NAME = "Test Token";
    private UserEntity userEntity;
    private TokenEntity tokenEntity;
    private Instant now;
    private Instant future;
    private Instant past;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        future = now.plusSeconds(3600); // 1 hour in the future
        past = now.minusSeconds(3600); // 1 hour in the past

        // Setup user entity
        userEntity = new UserEntity();
        userEntity.setId(TEST_USER_ID);
        userEntity.setUsername("testuser");

        // Setup token entity
        tokenEntity = new TokenEntity();
        tokenEntity.setId(TEST_TOKEN_ID);
        tokenEntity.setToken(TEST_TOKEN_STRING);
        tokenEntity.setName(TEST_TOKEN_NAME);
        tokenEntity.setUser(userEntity);
        tokenEntity.setExpiresAt(future);
        tokenEntity.setCreatedAt(now);
    }

    @Test
    void createToken_Success() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(userEntity));
        when(tokenRepository.save(any(TokenEntity.class))).thenReturn(tokenEntity);

        // Act
        Token result = tokenService.createToken(TEST_USER_ID, TEST_TOKEN_NAME, future);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_TOKEN_ID, result.id());
        assertEquals(TEST_TOKEN_STRING, result.token());
        assertEquals(TEST_TOKEN_NAME, result.name());
        assertEquals(TEST_USER_ID, result.userId());
        assertEquals(future, result.expiresAt());
        assertEquals(now, result.createdAt());

        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository).save(any(TokenEntity.class));
    }

    @Test
    void createToken_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                     () -> tokenService.createToken(TEST_USER_ID, TEST_TOKEN_NAME, future));
        
        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository, never()).save(any(TokenEntity.class));
    }

    @Test
    void getTokensByUserId_Success() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(userEntity));
        when(tokenRepository.findAllByUser(userEntity)).thenReturn(Arrays.asList(tokenEntity));

        // Act
        List<Token> results = tokenService.getTokensByUserId(TEST_USER_ID);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        Token result = results.get(0);
        assertEquals(TEST_TOKEN_ID, result.id());
        assertEquals(TEST_TOKEN_STRING, result.token());
        assertEquals(TEST_TOKEN_NAME, result.name());
        assertEquals(TEST_USER_ID, result.userId());
        assertEquals(future, result.expiresAt());
        assertEquals(now, result.createdAt());

        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository).findAllByUser(userEntity);
    }

    @Test
    void getTokensByUserId_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                     () -> tokenService.getTokensByUserId(TEST_USER_ID));
        
        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository, never()).findAllByUser(any(UserEntity.class));
    }

    @Test
    void deleteToken_Success() {
        // Act
        tokenService.deleteToken(TEST_TOKEN_STRING);

        // Assert
        verify(tokenRepository).deleteByToken(TEST_TOKEN_STRING);
    }

    @Test
    void deleteAllTokensByUserId_Success() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.of(userEntity));

        // Act
        tokenService.deleteAllTokensByUserId(TEST_USER_ID);

        // Assert
        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository).deleteAllByUser(userEntity);
    }

    @Test
    void deleteAllTokensByUserId_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findById(TEST_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
                     () -> tokenService.deleteAllTokensByUserId(TEST_USER_ID));
        
        verify(userRepository).findById(TEST_USER_ID);
        verify(tokenRepository, never()).deleteAllByUser(any(UserEntity.class));
    }

    @Test
    void validateToken_ValidToken_ReturnsUserId() {
        // Arrange
        when(tokenRepository.findByToken(TEST_TOKEN_STRING)).thenReturn(Optional.of(tokenEntity));

        // Act
        UUID result = tokenService.validateToken(TEST_TOKEN_STRING);

        // Assert
        assertNotNull(result);
        assertEquals(TEST_USER_ID, result);
        verify(tokenRepository).findByToken(TEST_TOKEN_STRING);
    }

    @Test
    void validateToken_ExpiredToken_ReturnsNull() {
        // Arrange
        TokenEntity expiredToken = new TokenEntity();
        expiredToken.setToken(TEST_TOKEN_STRING);
        expiredToken.setUser(userEntity);
        expiredToken.setExpiresAt(past); // Set to past time to make it expired

        when(tokenRepository.findByToken(TEST_TOKEN_STRING)).thenReturn(Optional.of(expiredToken));

        // Act
        UUID result = tokenService.validateToken(TEST_TOKEN_STRING);

        // Assert
        assertNull(result);
        verify(tokenRepository).findByToken(TEST_TOKEN_STRING);
    }

    @Test
    void validateToken_InvalidToken_ReturnsNull() {
        // Arrange
        when(tokenRepository.findByToken(TEST_TOKEN_STRING)).thenReturn(Optional.empty());

        // Act
        UUID result = tokenService.validateToken(TEST_TOKEN_STRING);

        // Assert
        assertNull(result);
        verify(tokenRepository).findByToken(TEST_TOKEN_STRING);
    }
}
