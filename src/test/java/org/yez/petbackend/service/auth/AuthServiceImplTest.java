package org.yez.petbackend.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yez.petbackend.domain.user.User;
import org.yez.petbackend.domain.user.UserRole;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.security.JwtService;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.group.GroupService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GroupService groupService;

    @InjectMocks
    private AuthServiceImpl authService;

    private UserRegisterDto registerDto;
    private LoginRequestDto loginRequestDto;
    private UserEntity userEntity;
    private final String TEST_USERNAME = "testuser";
    private final String TEST_PASSWORD = "password";
    private final String TEST_EMAIL = "test@example.com";
    private final String TEST_FULLNAME = "Test User";
    private final String TEST_TOKEN = "test.jwt.token";
    private final UUID TEST_USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Setup test data
        registerDto = new UserRegisterDto(TEST_USERNAME, TEST_PASSWORD, TEST_EMAIL, TEST_FULLNAME);
        loginRequestDto = new LoginRequestDto(TEST_USERNAME, TEST_PASSWORD);

        userEntity = new UserEntity();
        userEntity.setId(TEST_USER_ID);
        userEntity.setUsername(TEST_USERNAME);
        userEntity.setPassword(TEST_PASSWORD);
        userEntity.setEmail(TEST_EMAIL);
        userEntity.setFullName(TEST_FULLNAME);
        userEntity.setRole(UserRole.USER);
        userEntity.setApproved(true);
        userEntity.setGroups(new ArrayList<>());
    }

    @Test
    void registerUser_Success() {
        // Arrange
        when(userRepository.findByUsernameOrEmail(TEST_USERNAME, TEST_EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn("encodedPassword");

        // Mock the save method to set the ID and return the entity
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> {
            UserEntity savedUser = invocation.getArgument(0);
            savedUser.setId(TEST_USER_ID);
            return savedUser;
        });

        // Act
        authService.registerUser(registerDto);

        // Assert
        verify(userRepository).findByUsernameOrEmail(TEST_USERNAME, TEST_EMAIL);
        verify(passwordEncoder).encode(TEST_PASSWORD);
        verify(userRepository).save(any(UserEntity.class));
        verify(groupService).createDefault(eq(TEST_USER_ID), eq(TEST_USERNAME));
    }

    @Test
    void registerUser_UsernameOrEmailExists_ThrowsException() {
        // Arrange
        when(userRepository.findByUsernameOrEmail(TEST_USERNAME, TEST_EMAIL)).thenReturn(Optional.of(userEntity));

        // Act & Assert
        assertThrows(UsernameOrEmailExisted.class, () -> authService.registerUser(registerDto));
        verify(userRepository).findByUsernameOrEmail(TEST_USERNAME, TEST_EMAIL);
        verify(userRepository, never()).save(any(UserEntity.class));
        verify(groupService, never()).createDefault(any(UUID.class), anyString());
    }

    @Test
    void login_Success() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(true);
        when(jwtService.generateToken(TEST_USERNAME)).thenReturn(TEST_TOKEN);

        // Act
        LoginResponseDto response = authService.login(loginRequestDto);

        // Assert
        assertEquals(TEST_TOKEN, response.token());
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(passwordEncoder).matches(TEST_PASSWORD, TEST_PASSWORD);
        verify(jwtService).generateToken(TEST_USERNAME);
    }

    @Test
    void login_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotExistedException.class, () -> authService.login(loginRequestDto));
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void login_UserNotApproved_ThrowsException() {
        // Arrange
        userEntity.setApproved(false);
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));

        // Act & Assert
        assertThrows(UserNotExistedException.class, () -> authService.login(loginRequestDto));
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void login_IncorrectPassword_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));
        when(passwordEncoder.matches(TEST_PASSWORD, TEST_PASSWORD)).thenReturn(false);

        // Act & Assert
        assertThrows(UserPasswordNotMatched.class, () -> authService.login(loginRequestDto));
        verify(userRepository).findByUsername(TEST_USERNAME);
        verify(passwordEncoder).matches(TEST_PASSWORD, TEST_PASSWORD);
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void loadUserByUsername_Success() {
        // Arrange
        GroupEntity groupEntity = new GroupEntity();
        UUID groupId = UUID.randomUUID();
        groupEntity.setId(groupId);
        userEntity.getGroups().add(groupEntity);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.of(userEntity));

        // Act
        PetUser result = (PetUser) authService.loadUserByUsername(TEST_USERNAME);

        // Assert
        assertNotNull(result);
        User user = result.user();
        assertEquals(TEST_USER_ID, user.id());
        assertEquals(TEST_USERNAME, user.username());
        assertEquals(TEST_PASSWORD, user.password());
        assertEquals(TEST_EMAIL, user.email());
        assertEquals(TEST_FULLNAME, user.fullName());
        assertEquals(UserRole.USER, user.role());
        assertTrue(user.approved());
        assertTrue(user.groupIds().contains(groupId));

        verify(userRepository).findByUsername(TEST_USERNAME);
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        // Arrange
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.loadUserByUsername(TEST_USERNAME));
        verify(userRepository).findByUsername(TEST_USERNAME);
    }
}
