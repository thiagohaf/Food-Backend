package com.thiagoferreira.food_backend.services;

import com.thiagoferreira.food_backend.domain.entities.ErrorMessages;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.exceptions.DomainValidationException;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User createUser(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            new DomainValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
        System.out.println(user);
        return repository.save(user);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException((ErrorMessages.USER_NOT_FOUND_BY_ID)));
    }

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<User> searchByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public User updateUser(Long id, User dto) {
        User user = findById(id);
        user.setName(dto.getName());
        user.setAddress(dto.getAddress());
        user.setLastUpdated(LocalDateTime.now());
        return repository.save(user);
    }

    @Transactional
    public void changePassword(Long id, String currentPassword, String newPassword) {
        User user = findById(id);

        if (currentPassword.matches(newPassword)) {
            throw new DomainValidationException(ErrorMessages.PASSWORD_MISMATCH);
        }

        user.setPassword(newPassword);
        user.setLastUpdated(LocalDateTime.now());
        repository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
        repository.deleteById(id);
    }
}
