package com.thiagoferreira.food_backend.domain.dto;

import com.thiagoferreira.food_backend.domain.entities.UserType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String login;
    private UserType type;
    private AddressDTO address;
    private LocalDateTime lastUpdate;
}
