package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.dto.TokenResponse;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.infraestructure.security.JwtService;
import com.thiagoferreira.food_backend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerV2 implements AuthControllerV2Api {

    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<TokenResponse> login(
            LoginRequest loginRequest
    ) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        String token = jwtService.generateToken(user.getLogin(), user.getId());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @Override
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}

