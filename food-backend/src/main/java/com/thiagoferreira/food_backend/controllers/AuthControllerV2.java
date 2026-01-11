package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.dto.ProblemDetailDTO;
import com.thiagoferreira.food_backend.domain.dto.TokenResponse;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.infraestructure.security.JwtService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication V2", description = "Authentication APIs V2 - JWT token-based authentication")
public class AuthControllerV2 {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and get JWT token",
            description = "Authenticates a user with login and password. On success, returns a JWT token that must be included in the Authorization header for protected endpoints. " +
                    "Format: 'Bearer {token}'. This endpoint is public and does not require authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful - JWT token returned",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found or invalid credentials",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<TokenResponse> login(
            @RequestBody @Valid LoginRequest loginRequest
    ) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        String token = jwtService.generateToken(user.getLogin(), user.getId());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user",
            description = "Signals the client to discard the JWT token. Since JWT tokens are stateless, they remain valid until expiration. " +
                    "The client should discard the token after calling this endpoint. " +
                    "This endpoint requires authentication (valid JWT token)."
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful - token should be discarded by client",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - invalid or missing JWT token",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}

