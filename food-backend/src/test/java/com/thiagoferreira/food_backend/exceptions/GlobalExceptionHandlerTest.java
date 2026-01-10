package com.thiagoferreira.food_backend.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    @DisplayName("Should handle ResourceNotFoundException")
    void shouldHandleResourceNotFoundException() {
        // Arrange
        ResourceNotFoundException ex = new ResourceNotFoundException("User not found");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleResourceNotFoundException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User not found", response.getBody().getDetail());
        assertEquals("Resource Not Found", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("resource-not-found"));
    }

    @Test
    @DisplayName("Should handle UnauthorizedException with instance and properties")
    void shouldHandleUnauthorizedException() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException(
                com.thiagoferreira.food_backend.domain.enums.ErrorMessages.UNAUTHORIZED_ACCESS
        );
        
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn("/v1/users");
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURL()).thenReturn(new StringBuffer("http://localhost:8080/v1/users"));
        
        ServletWebRequest webRequest = new ServletWebRequest(request);

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleUnauthorizedException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("unauthorized"));
        assertNotNull(response.getBody().getInstance());
        assertTrue(response.getBody().getInstance().toString().contains("/v1/users"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("/v1/users", response.getBody().getProperties().get("path"));
        assertEquals("GET", response.getBody().getProperties().get("method"));
    }
    
    @Test
    @DisplayName("Should handle UnauthorizedException with non-ServletWebRequest")
    void shouldHandleUnauthorizedExceptionWithNonServletWebRequest() {
        // Arrange
        UnauthorizedException ex = new UnauthorizedException(
                com.thiagoferreira.food_backend.domain.enums.ErrorMessages.UNAUTHORIZED_ACCESS
        );
        
        WebRequest request = mock(WebRequest.class);
        when(request.getDescription(false)).thenReturn("uri=/v1/users");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleUnauthorizedException(ex, request);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unauthorized", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("unauthorized"));
        assertNotNull(response.getBody().getInstance());
        assertEquals("/v1/users", response.getBody().getInstance().toString());
    }

    @Test
    @DisplayName("Should handle DomainValidationException")
    void shouldHandleDomainValidationException() {
        // Arrange
        DomainValidationException ex = new DomainValidationException(
                com.thiagoferreira.food_backend.domain.enums.ErrorMessages.EMAIL_ALREADY_EXISTS
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleDomainValidationException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Domain Validation Error", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("domain-validation-error"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException")
    void shouldHandleMethodArgumentNotValidException() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("userRequest", "email", "Email is required");
        FieldError fieldError2 = new FieldError("userRequest", "name", "Name is required");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(new ArrayList<>(fieldErrors));
        when(ex.getMessage()).thenReturn("Validation error");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentNotValidException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation Error", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("validation-error"));
        assertNotNull(response.getBody().getProperties());
        assertTrue(response.getBody().getProperties().containsKey("errors"));
    }

    @Test
    @DisplayName("Should handle ConstraintViolationException")
    void shouldHandleConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = new HashSet<>(Arrays.asList(violation1, violation2));

        when(violation1.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation1.getPropertyPath().toString()).thenReturn("email");
        when(violation1.getMessage()).thenReturn("Email is invalid");

        when(violation2.getPropertyPath()).thenReturn(mock(jakarta.validation.Path.class));
        when(violation2.getPropertyPath().toString()).thenReturn("name");
        when(violation2.getMessage()).thenReturn("Name is required");

        ConstraintViolationException ex = new ConstraintViolationException("Constraint violation", violations);

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleConstraintViolationException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Constraint Violation", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("constraint-violation"));
        assertNotNull(response.getBody().getProperties());
        assertTrue(response.getBody().getProperties().containsKey("errors"));
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException - malformed JSON")
    void shouldHandleHttpMessageNotReadableExceptionMalformedJson() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getMessage()).thenReturn("Malformed JSON");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Malformed Request", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("malformed-request"));
        assertTrue(response.getBody().getDetail().contains("malformed"));
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException - missing body")
    void shouldHandleHttpMessageNotReadableExceptionMissingBody() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getMessage()).thenReturn("Required request body is missing");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getDetail().contains("required"));
    }

    @Test
    @DisplayName("Should handle HttpMessageNotReadableException - null message")
    void shouldHandleHttpMessageNotReadableExceptionNullMessage() {
        // Arrange
        HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
        when(ex.getMessage()).thenReturn(null);

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpMessageNotReadableException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Malformed Request", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("malformed-request"));
        assertTrue(response.getBody().getDetail().contains("malformed"));
    }

    @Test
    @DisplayName("Should handle MissingServletRequestParameterException")
    void shouldHandleMissingServletRequestParameterException() {
        // Arrange
        MissingServletRequestParameterException ex = new MissingServletRequestParameterException(
                "name", "String"
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMissingServletRequestParameterException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Missing Required Parameter", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("missing-parameter"));
        assertTrue(response.getBody().getDetail().contains("name"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("name", response.getBody().getProperties().get("parameter"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException")
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Arrange
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "123", Long.class, "id", null, null
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type Mismatch", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("type-mismatch"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("id", response.getBody().getProperties().get("parameter"));
        assertEquals("Long", response.getBody().getProperties().get("expectedType"));
        assertEquals("123", response.getBody().getProperties().get("providedValue"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException - null required type")
    void shouldHandleMethodArgumentTypeMismatchExceptionNullRequiredType() {
        // Arrange
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "123", null, "id", null, null
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type Mismatch", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("type-mismatch"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("id", response.getBody().getProperties().get("parameter"));
        assertEquals("unknown", response.getBody().getProperties().get("expectedType"));
        assertEquals("123", response.getBody().getProperties().get("providedValue"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException - null value")
    void shouldHandleMethodArgumentTypeMismatchExceptionNullValue() {
        // Arrange
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                null, Long.class, "id", null, null
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type Mismatch", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("type-mismatch"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("id", response.getBody().getProperties().get("parameter"));
        assertEquals("Long", response.getBody().getProperties().get("expectedType"));
        assertEquals("null", response.getBody().getProperties().get("providedValue"));
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException - null required type and null value")
    void shouldHandleMethodArgumentTypeMismatchExceptionNullRequiredTypeAndNullValue() {
        // Arrange
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                null, null, "id", null, null
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMethodArgumentTypeMismatchException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Type Mismatch", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("type-mismatch"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("id", response.getBody().getProperties().get("parameter"));
        assertEquals("unknown", response.getBody().getProperties().get("expectedType"));
        assertEquals("null", response.getBody().getProperties().get("providedValue"));
    }

    @Test
    @DisplayName("Should handle HttpRequestMethodNotSupportedException")
    void shouldHandleHttpRequestMethodNotSupportedException() {
        // Arrange
        Set<String> supportedMethods = new HashSet<>(Arrays.asList("GET", "POST"));
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException(
                "PUT", supportedMethods
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpRequestMethodNotSupportedException(ex);

        // Assert
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Method Not Allowed", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("method-not-allowed"));
        assertTrue(response.getBody().getDetail().contains("PUT"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("PUT", response.getBody().getProperties().get("method"));
    }

    @Test
    @DisplayName("Should handle HttpMediaTypeNotSupportedException")
    void shouldHandleHttpMediaTypeNotSupportedException() {
        // Arrange
        List<org.springframework.http.MediaType> supportedTypes = Arrays.asList(
                org.springframework.http.MediaType.APPLICATION_JSON
        );
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException(
                org.springframework.http.MediaType.APPLICATION_XML, supportedTypes
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpMediaTypeNotSupportedException(ex);

        // Assert
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unsupported Media Type", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("unsupported-media-type"));
        assertNotNull(response.getBody().getProperties());
        assertTrue(response.getBody().getProperties().get("contentType").toString().contains("application/xml"));
    }

    @Test
    @DisplayName("Should handle HttpMediaTypeNotSupportedException - null content type")
    void shouldHandleHttpMediaTypeNotSupportedExceptionNullContentType() {
        // Arrange
        List<org.springframework.http.MediaType> supportedTypes = Arrays.asList(
                org.springframework.http.MediaType.APPLICATION_JSON
        );
        HttpMediaTypeNotSupportedException ex = mock(HttpMediaTypeNotSupportedException.class);
        when(ex.getContentType()).thenReturn(null);
        when(ex.getSupportedMediaTypes()).thenReturn(supportedTypes);

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleHttpMediaTypeNotSupportedException(ex);

        // Assert
        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unsupported Media Type", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("unsupported-media-type"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("null", response.getBody().getProperties().get("contentType"));
    }

    @Test
    @DisplayName("Should handle MissingPathVariableException")
    void shouldHandleMissingPathVariableException() {
        // Arrange
        MissingPathVariableException ex = new MissingPathVariableException("id", null);

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleMissingPathVariableException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Missing Path Variable", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("missing-path-variable"));
        assertTrue(response.getBody().getDetail().contains("id"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("id", response.getBody().getProperties().get("variable"));
    }

    @Test
    @DisplayName("Should handle NoHandlerFoundException")
    void shouldHandleNoHandlerFoundException() {
        // Arrange
        NoHandlerFoundException ex = new NoHandlerFoundException(
                "GET", "/v1/invalid", null
        );

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleNoHandlerFoundException(ex);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Endpoint Not Found", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("endpoint-not-found"));
        assertTrue(response.getBody().getDetail().contains("GET"));
        assertTrue(response.getBody().getDetail().contains("/v1/invalid"));
        assertNotNull(response.getBody().getProperties());
        assertEquals("GET", response.getBody().getProperties().get("method"));
    }

    @Test
    @DisplayName("Should handle generic Exception")
    void shouldHandleGenericException() {
        // Arrange
        Exception ex = new Exception("Unexpected error");

        // Act
        ResponseEntity<ProblemDetail> response = exceptionHandler.handleGenericException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal Server Error", response.getBody().getTitle());
        assertTrue(response.getBody().getType().toString().contains("internal-server-error"));
        assertEquals("An unexpected error occurred", response.getBody().getDetail());
    }
}

