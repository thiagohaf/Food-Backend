package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.mappers.UserMapper;
import com.thiagoferreira.food_backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UserRequest userRequest
    ) {
        User user = userService.createUser(userMapper.toEntity(userRequest));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toResponse(user));
    }

    @GetMapping
    @Operation(summary = "Search users")
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

    @GetMapping("/search")
    @Operation(summary = "Search users by name")
    public ResponseEntity<List<UserResponse>> searchByName(
            @RequestParam String name
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

    @PutMapping("/{id}")
    @Operation(summary = "Update user info (except password)")
    public ResponseEntity<UserResponse> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    ) {
        User user = userService.findById(id);
        userMapper.updateEntityFromDto(userUpdateRequest, user);
        User updated = userService.updateUser(id, user);
        return ResponseEntity
                .ok(userMapper.toResponse(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
    ) {
        userService.changePassword(id, passwordChangeRequest.getCurrentPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity
                .noContent()
                .build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
