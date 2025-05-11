package org.yez.petbackend.controller.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.yez.petbackend.service.auth.AuthService;
import org.yez.petbackend.service.auth.LoginRequestDto;
import org.yez.petbackend.service.auth.LoginResponseDto;
import org.yez.petbackend.service.auth.UserRegisterDto;

@RestController
@RequestMapping("/auth")
class AuthController {

    private final AuthService authService;

    AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    void register(@RequestBody UserRegisterDto request) {
        authService.registerUser(request);
    }

    @PostMapping("/login")
    LoginResponseDto login(@RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
}
