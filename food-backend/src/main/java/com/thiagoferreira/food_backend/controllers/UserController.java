package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.mappers.UserMapper;
import com.thiagoferreira.food_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class UserController implements UserControllerApi {

    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public ResponseEntity<UserResponse> create(
            UserRequest userRequest
    ) {
        User user = userService.createUser(userMapper.toEntity(userRequest));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toResponse(user));
    }

    @Override
    public ResponseEntity<List<UserResponse>> findUsers() {
        List<User> users = userService.findUsers();
        List<UserResponse> response = Optional.ofNullable(users)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity
                .ok(response);
    }

    @Override
    public ResponseEntity<List<UserResponse>> searchByName(
            String name
    ) {
        List<User> users = userService.searchByName(name);
        List<UserResponse> response = Optional.ofNullable(users)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(userMapper::toResponse)
                .toList();
        return ResponseEntity
                .ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> searchByLogin(
            String login
    ) {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @Override
    public ResponseEntity<UserResponse> searchByEmail(
            String email
    ) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @Override
    public ResponseEntity<UserResponse> searchById(
            Long id
    ) {
        User user = userService.findById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity
                .ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> updateInfo(
            Long id,
            UserUpdateRequest userUpdateRequest
    ) {
        User user = userService.findById(id);
        userMapper.updateEntityFromDto(userUpdateRequest, user);
        User updated = userService.updateUser(id, user);
        return ResponseEntity
                .ok(userMapper.toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> changePassword(
            Long id,
            PasswordChangeRequest passwordChangeRequest
    ) {
        userService.changePassword(id, passwordChangeRequest.getCurrentPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity
                .noContent()
                .build();
    }

    @Override
    public ResponseEntity<Void> delete(
            Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
