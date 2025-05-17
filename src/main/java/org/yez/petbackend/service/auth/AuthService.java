package org.yez.petbackend.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AuthService extends UserDetailsService {
    void registerUser(UserRegisterDto request);

    LoginResponseDto login(LoginRequestDto request);

    UserDetails loadUserById(UUID userId);
}
