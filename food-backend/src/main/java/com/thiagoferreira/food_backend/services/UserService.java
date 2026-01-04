package com.thiagoferreira.food_backend.services;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.exceptions.DomainValidationException;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.infraestructure.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
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
            throw new DomainValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
        if (repository.existsByLogin(user.getLogin())) {
            throw new DomainValidationException(ErrorMessages.LOGIN_ALREADY_EXISTS);
        }
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
        return repository.save(user);
    }

    public List<User> findUsers() {
        return repository.findAll();
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND_BY_ID.params(id)));
    }

    public Optional<User> findByLogin(String login) {
        return repository.findByLogin(login);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User authenticate(String login, String password) {
        User user = repository.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
        
        return user;
    }

    public List<User> searchByName(String name) {
        return repository.findByNameContainingIgnoreCaseOrderByNameAsc(name);
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

        if (!BCrypt.checkpw(currentPassword, user.getPassword())) {
            throw new DomainValidationException(ErrorMessages.PASSWORD_MISMATCH);
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPassword(hashedPassword);
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
