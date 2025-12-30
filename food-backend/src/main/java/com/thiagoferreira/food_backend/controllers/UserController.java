package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
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
@RequestMapping("/users")
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
    public ResponseEntity<List<User>> searchByName(
            @RequestParam String name
    ) {
        List<User> users = userService.searchByName(name);
        return ResponseEntity
                .ok(users.stream().toList());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user info (except password)")
    public ResponseEntity<User> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid User dto
    ) {
        User updated = userService.updateUser(id, dto);
        return ResponseEntity
                .ok(updated);
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
