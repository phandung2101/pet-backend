package org.yez.petbackend.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.domain.user.UserRole;
import org.yez.petbackend.security.JwtService;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.group.GroupService;

@Service
record AuthServiceImpl(
        UserRepository userRepository,
        JwtService jwtService,
        PasswordEncoder passwordEncoder,
        GroupService groupService
) implements AuthService {

    @Override
    public void registerUser(final UserRegisterDto request) {
        if (userRepository.findByUsernameOrEmail(request.username(), request.email()).isPresent()) {
            throw new UsernameOrEmailExisted();
        }
        var newUser = new UserEntity();
        newUser.setUsername(request.username());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setRole(UserRole.USER);
        newUser.setApproved(false);
        newUser.setFullName(request.fullName());

        userRepository.save(newUser);
        groupService.createDefault(newUser);
    }

    @Override
    public LoginResponseDto login(final LoginRequestDto request) {
        var user = userRepository.findByUsername(request.username()).orElseThrow(UserNotExistedException::new);
        if (!user.isApproved()) throw new UserNotExistedException();
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new UserPasswordNotMatched();
        }
        var token = jwtService.generateToken(user.getUsername());
        return new LoginResponseDto(token);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return new PetUser(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
    }
}
