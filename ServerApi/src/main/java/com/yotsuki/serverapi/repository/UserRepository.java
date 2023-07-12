package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String  username);

    boolean existsByEmail(String email);
}
