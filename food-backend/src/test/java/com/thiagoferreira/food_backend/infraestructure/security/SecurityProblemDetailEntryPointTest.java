package com.thiagoferreira.food_backend.infraestructure.security;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityProblemDetailEntryPoint Tests")
class SecurityProblemDetailEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private SecurityProblemDetailEntryPoint entryPoint;

    private ByteArrayOutputStream outputStream;
    private ServletOutputStream servletOutputStream;

    @BeforeEach
    void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                // Not needed for testing
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);
    }

    @Test
    @DisplayName("Should return ProblemDetail with instance and properties on authentication failure")
    void shouldReturnProblemDetailWithInstanceAndProperties() throws Exception {
        // Arrange
        String requestURI = "/v2/users";
        String requestURL = "http://localhost:8080/v2/users";
        String method = "GET";

        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        when(request.getMethod()).thenReturn(method);
        doNothing().when(response).setContentType(anyString());
        doNothing().when(response).setStatus(anyInt());

        BadCredentialsException authException = new BadCredentialsException("Bad credentials");

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpStatus.UNAUTHORIZED.value());

        String jsonResponse = outputStream.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains("unauthorized"));
        assertTrue(jsonResponse.contains("Authentication required"));
        assertTrue(jsonResponse.contains("instance"));
        assertTrue(jsonResponse.contains(requestURL));
        assertTrue(jsonResponse.contains("properties"));
        assertTrue(jsonResponse.contains("\"path\""));
        assertTrue(jsonResponse.contains("\"method\""));
        assertTrue(jsonResponse.contains(requestURI));
        assertTrue(jsonResponse.contains(method));
    }

    @Test
    @DisplayName("Should include query parameters in instance URI")
    void shouldIncludeQueryParametersInInstance() throws Exception {
        // Arrange
        String requestURI = "/v2/users";
        String requestURL = "http://localhost:8080/v2/users?page=1&size=10";
        String method = "GET";

        when(request.getRequestURI()).thenReturn(requestURI);
        when(request.getRequestURL()).thenReturn(new StringBuffer(requestURL));
        when(request.getMethod()).thenReturn(method);
        doNothing().when(response).setContentType(anyString());
        doNothing().when(response).setStatus(anyInt());

        BadCredentialsException authException = new BadCredentialsException("Bad credentials");

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        String jsonResponse = outputStream.toString();
        assertNotNull(jsonResponse);
        assertTrue(jsonResponse.contains(requestURL));
    }
}
