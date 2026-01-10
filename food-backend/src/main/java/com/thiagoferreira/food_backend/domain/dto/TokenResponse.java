package com.thiagoferreira.food_backend.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponse {
    private String token;
    private String type;
    
    public TokenResponse(String token) {
        this.token = token;
        this.type = "Bearer";
    }
}

