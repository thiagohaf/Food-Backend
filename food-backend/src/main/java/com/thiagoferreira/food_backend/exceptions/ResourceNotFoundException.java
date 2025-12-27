package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.entities.ErrorMessages;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(ErrorMessages errorMessages) {
        super(errorMessages.getMessage());
    }
}
