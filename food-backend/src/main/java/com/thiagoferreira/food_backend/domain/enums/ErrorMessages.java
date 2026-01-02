package com.thiagoferreira.food_backend.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessages {

    // --- User Related Errors ---
    USER_NOT_FOUND("User not found with the provided details."),
    USER_NOT_FOUND_BY_ID("User not found with ID: %s"),
    EMAIL_ALREADY_EXISTS("The email provided is already registered."),
    LOGIN_ALREADY_EXISTS("The login provided is already registered."),
    LOGIN_INVALID("Invalid credentials. Please check your login and password."),
    PASSWORD_MISMATCH("The current password provided is incorrect."),

    // --- Validation/Generic Errors ---
    INVALID_REQUEST("The request contains invalid data."),
    INTERNAL_SERVER_ERROR("An unexpected internal error occurred.");

    private final String message;

    public String params(Object... args) {
        return String.format(this.message, args);
    }
}
