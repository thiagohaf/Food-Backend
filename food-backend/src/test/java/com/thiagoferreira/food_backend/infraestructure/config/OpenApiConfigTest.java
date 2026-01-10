package com.thiagoferreira.food_backend.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.models.GroupedOpenApi;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenApiConfig Tests")
class OpenApiConfigTest {

    @InjectMocks
    private OpenApiConfig openApiConfig;

    @Test
    @DisplayName("Should create OpenAPI bean with shared components successfully")
    void shouldCreateOpenApiBean() {
        // Act
        OpenAPI openAPI = openApiConfig.springShopOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getComponents());
        assertNotNull(openAPI.getComponents().getSecuritySchemes());
        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("bearerAuth"));
        assertNotNull(openAPI.getExternalDocs());
        // Info should not be set in global bean, only in groups
        assertNull(openAPI.getInfo());
    }

    @Test
    @DisplayName("Should create V1 GroupedOpenApi bean successfully")
    void shouldCreateV1GroupedOpenApiBean() {
        // Act
        GroupedOpenApi v1GroupedOpenApi = openApiConfig.v1Api();

        // Assert
        assertNotNull(v1GroupedOpenApi);
        assertEquals("v1", v1GroupedOpenApi.getGroup());
    }

    @Test
    @DisplayName("Should create V2 GroupedOpenApi bean successfully")
    void shouldCreateV2GroupedOpenApiBean() {
        // Act
        GroupedOpenApi v2GroupedOpenApi = openApiConfig.v2Api();

        // Assert
        assertNotNull(v2GroupedOpenApi);
        assertEquals("v2", v2GroupedOpenApi.getGroup());
    }
}

