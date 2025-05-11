package org.yez.petbackend.service.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    void registerUser(UserRegisterDto request);

    LoginResponseDto login(LoginRequestDto request);
}
