package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.entities.User;
import com.thiagoferreira.food_backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management APIs")
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Create a new user")
    public ResponseEntity<User> create(
            @RequestBody @Valid User dto
    ) {
        User created = userService.createUser(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
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
            @RequestBody @Valid String currentPassword,
            @RequestBody @Valid String newPassword
    ) {
        userService.changePassword(id, currentPassword, newPassword);
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
