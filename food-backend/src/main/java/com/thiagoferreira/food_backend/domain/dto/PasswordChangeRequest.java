package com.thiagoferreira.food_backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeRequest {

    @NotBlank(message = "The current password is required.")
    private String currentPassword;

    @NotBlank(message = "The new password is required.")
    @Size(min = 6, message = "New password must be at least 6 characters")
    private String newPassword;
}
