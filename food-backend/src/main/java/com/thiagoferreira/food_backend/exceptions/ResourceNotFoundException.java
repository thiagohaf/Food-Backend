package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(ErrorMessages errorMessages) {
        super(errorMessages.getMessage());
    }
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
