package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DomainValidationException Tests")
class DomainValidationExceptionTest {

    @Test
    @DisplayName("Should create exception with ErrorMessages")
    void shouldCreateExceptionWithErrorMessages() {
        // Act
        DomainValidationException exception = new DomainValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS);

        // Assert
        assertNotNull(exception);
        assertEquals(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Should create exception with ErrorMessages and cause")
    void shouldCreateExceptionWithErrorMessagesAndCause() {
        // Arrange
        Throwable cause = new RuntimeException("Root cause");

        // Act
        DomainValidationException exception = new DomainValidationException(ErrorMessages.EMAIL_ALREADY_EXISTS, cause);

        // Assert
        assertNotNull(exception);
        assertEquals(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}

