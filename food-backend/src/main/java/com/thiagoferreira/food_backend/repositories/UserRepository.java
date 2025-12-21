package com.thiagoferreira.food_backend.repositories;

import com.thiagoferreira.food_backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    List<User> findByName(String name);
    boolean existsByEmail(String email);
}
