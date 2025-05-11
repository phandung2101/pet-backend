package org.yez.petbackend.service.user;

import org.springframework.stereotype.Service;
import org.yez.petbackend.repository.user.UserRepository;
import org.yez.petbackend.service.auth.UserNotExistedException;

import java.util.UUID;

@Service
public record AdminServiceImpl(
        UserRepository userRepository
) implements AdminService {

    @Override
    public void approveUser(final UUID userId) {
        var user = userRepository.findById(userId).orElseThrow(UserNotExistedException::new);
        user.setApproved(true);
        userRepository.save(user);
    }
}
