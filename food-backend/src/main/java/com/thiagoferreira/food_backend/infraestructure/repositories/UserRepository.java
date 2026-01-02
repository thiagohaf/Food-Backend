package com.thiagoferreira.food_backend.infraestructure.repositories;

import com.thiagoferreira.food_backend.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByLogin(String login);
    Optional<User> findById(Long id);
    List<User> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
    boolean existsByEmail(String email);
}
