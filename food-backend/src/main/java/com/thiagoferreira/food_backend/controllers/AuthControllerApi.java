package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.dto.ProblemDetailDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication APIs - Stateful session-based authentication")
public interface AuthControllerApi {

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate user and create session",
            description = "Authenticates a user with login and password. On success, creates an HTTP session and stores the user ID. " +
                    "The session is maintained via cookies (JSESSIONID). This endpoint is public and does not require authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful - session created",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "User not found or invalid credentials",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "415", description = "Unsupported media type",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    ResponseEntity<Void> login(
            @RequestBody @Valid LoginRequest loginRequest,
            HttpServletRequest request
    );

    @PostMapping("/logout")
    @Operation(
            summary = "Logout user and invalidate session",
            description = "Invalidates the current HTTP session, effectively logging out the user. " +
                    "After logout, the user will need to login again to access protected endpoints. " +
                    "This endpoint requires authentication (valid session)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful - session invalidated",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized - no valid session",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetailDTO.class)))
    })
    ResponseEntity<Void> logout(HttpServletRequest request);
}
