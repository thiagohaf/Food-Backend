package com.thiagoferreira.food_backend.services;

import com.thiagoferreira.food_backend.entities.User;
import com.thiagoferreira.food_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User createUser(User user) throws Exception {
        if (repository.existsByEmail(user.getEmail())) {
            throw new Exception("E-mail already registered");
        }
        return repository.save(user);
    }

    public User findById(Long id) throws Exception {
        return repository.findById(id)
                .orElseThrow( () -> new Exception("User not found"));
    }

    public List<User> searchByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public User updateUser(Long id, User dto) throws Exception {
        try {
            User user = findById(id);
            user.setName(dto.getName());
            user.setAddress(dto.getAddress());
            user.setLastUpdated(LocalDateTime.now());
            return repository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) throws Exception {
        try {
            User user = findById(id);

            if (currentPassword.matches(newPassword)) {
                throw new RuntimeException("Invalid current password.");
            }

            user.setPassword(newPassword);
            user.setLastUpdated(LocalDateTime.now());
            repository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteUser(Long id) throws Exception {
        if (!repository.existsById(id)) {
            throw new Exception("User not found");
        }
        repository.deleteById(id);
    }
}
