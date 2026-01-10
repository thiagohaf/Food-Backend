package com.thiagoferreira.food_backend.infraestructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

@Component
@Slf4j
public class SecurityProblemDetailAccessDeniedHandler implements AccessDeniedHandler {

    private static final String PROBLEM_TYPE_BASE_URI = "https://api.food-backend.com/problems/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error("Access denied: {}", accessDeniedException.getMessage());

        String requestURI = request.getRequestURI();

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN,
                "You do not have permission to access this resource."
        );
        problemDetail.setType(URI.create(PROBLEM_TYPE_BASE_URI + "forbidden"));
        problemDetail.setTitle("Forbidden");
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        problemDetail.setProperty("path", requestURI);
        problemDetail.setProperty("method", request.getMethod());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        objectMapper.writeValue(response.getOutputStream(), problemDetail);
    }
}
