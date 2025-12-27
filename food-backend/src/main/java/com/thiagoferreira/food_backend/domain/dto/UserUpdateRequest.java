package com.thiagoferreira.food_backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private AddressDTO address;
}
