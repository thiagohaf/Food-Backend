package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.LoginRequest;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerApi {

    private final UserService userService;

    @Override
    public ResponseEntity<Void> login(
            LoginRequest loginRequest,
            HttpServletRequest request
    ) {
        User user = userService.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        HttpSession session = request.getSession(true);
        session.setAttribute("USER_ID", user.getId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

