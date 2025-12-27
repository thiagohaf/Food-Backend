package com.thiagoferreira.food_backend.domain.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private boolean success;
    private String message;
}
