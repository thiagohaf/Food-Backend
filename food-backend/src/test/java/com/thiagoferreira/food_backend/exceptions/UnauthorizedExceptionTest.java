package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UnauthorizedException Tests")
class UnauthorizedExceptionTest {

    @Test
    @DisplayName("Should create exception with ErrorMessages")
    void shouldCreateExceptionWithErrorMessages() {
        // Act
        UnauthorizedException exception = new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);

        // Assert
        assertNotNull(exception);
        assertEquals(ErrorMessages.UNAUTHORIZED_ACCESS.getMessage(), exception.getMessage());
    }
}

