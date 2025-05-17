package org.yez.petbackend.repository.token;

import org.springframework.data.repository.CrudRepository;
import org.yez.petbackend.repository.user.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends CrudRepository<TokenEntity, UUID> {
    Optional<TokenEntity> findByToken(String token);
    
    List<TokenEntity> findAllByUser(UserEntity user);
    
    void deleteByToken(String token);
    
    void deleteAllByUser(UserEntity user);
}
