package com.thiagoferreira.food_backend.interceptors;

import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if ("OPTIONS".equals(method)) {
            return true;
        }

        if (uri.startsWith("/auth/login")) {
            return true;
        }

        if (uri.equals("/v1/users") && "POST".equals(method)) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("USER_ID") == null) {
            throw new UnauthorizedException(ErrorMessages.UNAUTHORIZED_ACCESS);
        }

        return true;
    }
}

