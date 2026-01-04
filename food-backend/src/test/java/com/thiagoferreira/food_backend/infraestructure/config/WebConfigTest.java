package com.thiagoferreira.food_backend.infraestructure.config;

import com.thiagoferreira.food_backend.interceptors.AuthInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WebConfig Tests")
class WebConfigTest {

    @Mock
    private AuthInterceptor authInterceptor;

    @Mock
    private InterceptorRegistry registry;

    @Mock
    private InterceptorRegistration interceptorRegistration;

    private WebConfig webConfig;

    @BeforeEach
    void setUp() {
        webConfig = new WebConfig(authInterceptor);
    }

    @Test
    @DisplayName("Should add interceptor to registry")
    void shouldAddInterceptorToRegistry() {
        // Arrange
        when(registry.addInterceptor(authInterceptor)).thenReturn(interceptorRegistration);
        when(interceptorRegistration.addPathPatterns("/**")).thenReturn(interceptorRegistration);

        // Act
        webConfig.addInterceptors(registry);

        // Assert
        verify(registry, times(1)).addInterceptor(authInterceptor);
        verify(interceptorRegistration, times(1)).addPathPatterns("/**");
    }
}

