package com.thiagoferreira.food_backend.interceptors;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthInterceptor Tests")
class AuthInterceptorTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Object handler;

    @InjectMocks
    private AuthInterceptor authInterceptor;

    @BeforeEach
    void setUp() {
        // Setup default mocks
    }

    @Test
    @DisplayName("Should allow OPTIONS request")
    void shouldAllowOptionsRequest() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("OPTIONS");
        when(request.getRequestURI()).thenReturn("/v1/users");

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request, never()).getSession(anyBoolean());
    }

    @Test
    @DisplayName("Should allow login endpoint without authentication")
    void shouldAllowLoginEndpoint() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/auth/login");

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request, never()).getSession(anyBoolean());
    }

    @Test
    @DisplayName("Should allow POST /v1/users without authentication")
    void shouldAllowPostUsersEndpoint() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/v1/users");

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request, never()).getSession(anyBoolean());
    }

    @Test
    @DisplayName("Should allow request when valid session exists")
    void shouldAllowRequestWithValidSession() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/v1/users/1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request, times(1)).getSession(false);
        verify(session, times(1)).getAttribute("USER_ID");
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when session is null")
    void shouldThrowExceptionWhenSessionIsNull() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/v1/users/1");
        when(request.getSession(false)).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
        verify(request, times(1)).getSession(false);
    }

    @Test
    @DisplayName("Should throw UnauthorizedException when USER_ID is null in session")
    void shouldThrowExceptionWhenUserIdIsNull() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/v1/users/1");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("USER_ID")).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
        verify(request, times(1)).getSession(false);
        verify(session, times(1)).getAttribute("USER_ID");
    }

    @Test
    @DisplayName("Should require authentication for GET /v1/users")
    void shouldRequireAuthenticationForGetUsers() {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getSession(false)).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should require authentication for PUT /v1/users/{id}")
    void shouldRequireAuthenticationForPutUsers() {
        // Arrange
        when(request.getMethod()).thenReturn("PUT");
        when(request.getRequestURI()).thenReturn("/v1/users/1");
        when(request.getSession(false)).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should require authentication for DELETE /v1/users/{id}")
    void shouldRequireAuthenticationForDeleteUsers() {
        // Arrange
        when(request.getMethod()).thenReturn("DELETE");
        when(request.getRequestURI()).thenReturn("/v1/users/1");
        when(request.getSession(false)).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should require authentication for PATCH /v1/users/{id}/password")
    void shouldRequireAuthenticationForPatchPassword() {
        // Arrange
        when(request.getMethod()).thenReturn("PATCH");
        when(request.getRequestURI()).thenReturn("/v1/users/1/password");
        when(request.getSession(false)).thenReturn(null);

        // Act & Assert
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> authInterceptor.preHandle(request, response, handler)
        );
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should allow request when session has USER_ID")
    void shouldAllowRequestWhenSessionHasUserId() throws Exception {
        // Arrange
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/v1/users/search/name");
        when(request.getRequestURI()).thenReturn("/v1/users/search/name?name=test");
        when(request.getSession(false)).thenReturn(session);
        when(session.getAttribute("USER_ID")).thenReturn(1L);

        // Act
        boolean result = authInterceptor.preHandle(request, response, handler);

        // Assert
        assertTrue(result);
        verify(request, times(1)).getSession(false);
        verify(session, times(1)).getAttribute("USER_ID");
    }
}

