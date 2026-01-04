package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(ErrorMessages errorMessages) {
        super(errorMessages.getMessage());
    }
}

