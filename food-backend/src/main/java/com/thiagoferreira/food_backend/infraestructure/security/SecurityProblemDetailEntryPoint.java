package com.thiagoferreira.food_backend.infraestructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
@Slf4j
public class SecurityProblemDetailEntryPoint implements AuthenticationEntryPoint {

    private static final String PROBLEM_TYPE_BASE_URI = "https://api.food-backend.com/problems/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        log.error("Authentication failed: {}", authException.getMessage());

        String requestURI = request.getRequestURI();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                "Authentication required. Please provide a valid JWT token in the Authorization header."
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "unauthorized"));
        problemDetail.setTitle("Unauthorized");
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        problemDetail.setProperty("path", requestURI);
        problemDetail.setProperty("method", request.getMethod());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}
