package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.dto.TokenResponse;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.UserType;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.infraestructure.security.JwtService;
import com.thiagoferreira.food_backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthControllerV2 Tests")
class AuthControllerV2Test {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthControllerV2 authControllerV2;

    private User user;
    private LoginRequest loginRequest;
    private String token;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setPassword("hashedpassword");
        user.setType(UserType.CUSTOMER);

        loginRequest = new LoginRequest();
        loginRequest.setLogin("testuser");
        loginRequest.setPassword("password123");

        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.test.token";
    }

    @Test
    @DisplayName("Should login successfully and return JWT token")
    void shouldLoginSuccessfully() {
        // Arrange
        when(userService.authenticate("testuser", "password123")).thenReturn(user);
        when(jwtService.generateToken("testuser", 1L)).thenReturn(token);

        // Act
        ResponseEntity<TokenResponse> response = authControllerV2.login(loginRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(token, response.getBody().getToken());
        assertEquals("Bearer", response.getBody().getType());
        verify(userService, times(1)).authenticate("testuser", "password123");
        verify(jwtService, times(1)).generateToken("testuser", 1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when login fails")
    void shouldThrowExceptionWhenLoginFails() {
        // Arrange
        when(userService.authenticate(anyString(), anyString()))
                .thenThrow(new ResourceNotFoundException("User not found"));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> authControllerV2.login(loginRequest)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).authenticate("testuser", "password123");
        verify(jwtService, never()).generateToken(anyString(), any());
    }

    @Test
    @DisplayName("Should logout successfully")
    void shouldLogoutSuccessfully() {
        // Act
        ResponseEntity<Void> response = authControllerV2.logout();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
