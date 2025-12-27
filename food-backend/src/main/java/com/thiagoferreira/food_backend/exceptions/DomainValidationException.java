package com.thiagoferreira.food_backend.exceptions;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;

public class DomainValidationException extends RuntimeException {
    public DomainValidationException(ErrorMessages errorMessages) { super(errorMessages.getMessage()); }

    public DomainValidationException(ErrorMessages errorMessages, Throwable cause) { super(errorMessages.getMessage(), cause); }
}
