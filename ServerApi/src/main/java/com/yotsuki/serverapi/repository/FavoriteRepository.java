package com.yotsuki.serverapi.repository;

import com.yotsuki.serverapi.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    Optional<Favorite> findByUserId(Long userId);
}
