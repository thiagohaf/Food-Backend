package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.UserType;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AuthController authController;

    private User user;
    private LoginRequest loginRequest;

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
    }

    @Test
    @DisplayName("Should login successfully and create session")
    void shouldLoginSuccessfully() {
        // Arrange
        when(userService.authenticate("testuser", "password123")).thenReturn(user);
        when(request.getSession(true)).thenReturn(session);
        doNothing().when(session).setAttribute("USER_ID", 1L);

        // Act
        ResponseEntity<Void> response = authController.login(loginRequest, request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).authenticate("testuser", "password123");
        verify(request, times(1)).getSession(true);
        verify(session, times(1)).setAttribute("USER_ID", 1L);
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
                () -> authController.login(loginRequest, request)
        );
        assertEquals("User not found", exception.getMessage());
        verify(userService, times(1)).authenticate("testuser", "password123");
        verify(request, never()).getSession(anyBoolean());
    }

    @Test
    @DisplayName("Should logout successfully when session exists")
    void shouldLogoutSuccessfullyWhenSessionExists() {
        // Arrange
        when(request.getSession(false)).thenReturn(session);
        doNothing().when(session).invalidate();

        // Act
        ResponseEntity<Void> response = authController.logout(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(request, times(1)).getSession(false);
        verify(session, times(1)).invalidate();
    }

    @Test
    @DisplayName("Should logout successfully when session does not exist")
    void shouldLogoutSuccessfullyWhenSessionDoesNotExist() {
        // Arrange
        when(request.getSession(false)).thenReturn(null);

        // Act
        ResponseEntity<Void> response = authController.logout(request);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(request, times(1)).getSession(false);
        verify(session, never()).invalidate();
    }
}

