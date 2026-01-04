package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ResourceNotFoundException Tests")
class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Should create exception with ErrorMessages")
    void shouldCreateExceptionWithErrorMessages() {
        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(ErrorMessages.USER_NOT_FOUND);

        // Assert
        assertNotNull(exception);
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("Should create exception with String message")
    void shouldCreateExceptionWithStringMessage() {
        // Arrange
        String message = "Custom error message";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }
}

