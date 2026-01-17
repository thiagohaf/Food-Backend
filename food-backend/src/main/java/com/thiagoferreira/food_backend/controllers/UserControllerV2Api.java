package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.ProblemDetailDTO;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/v2/users")
@Tag(name = "Users V2", description = "User management APIs V2. Most endpoints require JWT authentication. " +
        "Only POST /v2/users (create user) is public and does not require authentication. " +
        "Use 'Bearer {token}' in Authorization header for protected endpoints.")
@SecurityRequirement(name = "bearerAuth")
public interface UserControllerV2Api {

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
    ResponseEntity<UserResponse> create(
            @RequestBody @Valid UserRequest userRequest
    );

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
    ResponseEntity<List<UserResponse>> findUsers();

    @GetMapping("/search/name")
    @Operation(
            summary = "Search users by name",
            description = "Searches users by name (partial match, case-insensitive). If no name is provided, returns all users. Requires JWT authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token required",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    ResponseEntity<List<UserResponse>> searchByName(
            @RequestParam(required = false) String name
    );

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
    ResponseEntity<UserResponse> searchByLogin(
            @RequestParam String login
    );

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
    ResponseEntity<UserResponse> searchByEmail(
            @RequestParam String email
    );

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
    ResponseEntity<UserResponse> searchById(
            @PathVariable Long id
    );

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
    ResponseEntity<UserResponse> updateInfo(
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest userUpdateRequest
    );

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
    ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody @Valid PasswordChangeRequest passwordChangeRequest
    );

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
    ResponseEntity<Void> delete(
            @PathVariable Long id
    );
}
