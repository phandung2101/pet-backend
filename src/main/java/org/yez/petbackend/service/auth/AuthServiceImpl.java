package org.yez.petbackend.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yez.petbackend.domain.user.User;
import org.yez.petbackend.domain.user.UserRole;
import org.yez.petbackend.repository.group.GroupEntity;
import org.yez.petbackend.repository.user.UserEntity;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.security.JwtService;
import org.yez.petbackend.security.PetUser;
import org.yez.petbackend.service.group.GroupService;

import java.util.UUID;
import java.util.stream.Collectors;

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
        groupService.createDefault(newUser.getId(), newUser.getUsername());
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
        var userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
        return createPetUser(userEntity);
    }

    @Override
    public UserDetails loadUserById(final UUID userId) {
        var userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
        return createPetUser(userEntity);
    }

    private PetUser createPetUser(UserEntity userEntity) {
        var user = new User(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getEmail(),
                userEntity.getFullName(),
                userEntity.getRole(),
                userEntity.isApproved(),
                userEntity.getGroups().stream().map(GroupEntity::getId).collect(Collectors.toSet())
        );
        return new PetUser(user);
    }
}
