package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.ProblemDetailDTO;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.mappers.UserMapper;
import com.thiagoferreira.food_backend.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@RequestMapping("/v2/users")
@RequiredArgsConstructor
@Tag(name = "Users V2", description = "User management APIs V2. Most endpoints require JWT authentication. " +
        "Only POST /v2/users (create user) is public and does not require authentication. " +
        "Use 'Bearer {token}' in Authorization header for protected endpoints.")
@SecurityRequirement(name = "bearerAuth")
public class UserControllerV2 {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @Operation(
            summary = "Create a new user",
            description = "Creates a new user. This endpoint is PUBLIC and does not require authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or domain validation error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<UserResponse> create(
            @RequestBody @Valid UserRequest userRequest
    ) {
        User user = userService.createUser(userMapper.toEntity(userRequest));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toResponse(user));
    }

    @GetMapping
    @Operation(
            summary = "Search users",
            description = "Lists all users. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<List<UserResponse>> findUsers() {
        List<User> users = userService.findUsers();
        List<UserResponse> response = Optional.ofNullable(users)
                .orElse(Collections.emptyList())
                .stream()
                .filter(Objects::nonNull)
                .map(userMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/name")
    @Operation(
            summary = "Search users by name",
            description = "Searches users by name (partial match, case-insensitive). Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully"),
            @ApiResponse(responseCode = "400", description = "Missing required parameter",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
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
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/login")
    @Operation(
            summary = "Search users by login",
            description = "Searches a user by login. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "400", description = "Missing required parameter",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<UserResponse> searchByLogin(
            @RequestParam String login
    ) {
        User user = userService.findByLogin(login)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/search/email")
    @Operation(
            summary = "Search users by email",
            description = "Searches a user by email. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "400", description = "Missing required parameter",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<UserResponse> searchByEmail(
            @RequestParam String email
    ) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND));
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Search users by id",
            description = "Searches a user by ID. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<UserResponse> searchById(
            @PathVariable Long id
    ) {
        User user = userService.findById(id);
        UserResponse response = userMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update user info (except password)",
            description = "Updates user information (name and address). Password cannot be updated through this endpoint. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid ID format",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<UserResponse> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    ) {
        User user = userService.findById(id);
        userMapper.updateEntityFromDto(userUpdateRequest, user);
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(userMapper.toResponse(updated));
    }

    @PatchMapping("/{id}/password")
    @Operation(
            summary = "Change user password",
            description = "Changes the user's password. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error, domain validation error or invalid ID format",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
    ) {
        userService.changePassword(id, passwordChangeRequest.getCurrentPassword(), passwordChangeRequest.getNewPassword());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user by ID. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

