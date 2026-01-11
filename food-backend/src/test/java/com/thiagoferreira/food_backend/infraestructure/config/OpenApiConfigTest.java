package com.thiagoferreira.food_backend.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Test
    @DisplayName("Should apply V1 API customizer successfully")
    void shouldApplyV1ApiCustomizerSuccessfully() {
        // Arrange
        OpenAPI openAPI = new OpenAPI();
        OpenApiCustomizer customizer = (OpenApiCustomizer) ReflectionTestUtils.invokeMethod(openApiConfig, "v1ApiCustomizer");

        // Act
        customizer.customise(openAPI);

        // Assert
        assertNotNull(openAPI.getInfo());
        assertEquals("API Core - V1 (Legado)", openAPI.getInfo().getTitle());
        assertEquals("v1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
        assertTrue(openAPI.getInfo().getDescription().contains("API RESTful para o sistema de gestão de restaurantes Food App - Versão 1"));
        assertTrue(openAPI.getInfo().getDescription().contains("HttpSession"));
        assertNotNull(openAPI.getInfo().getLicense());
        assertEquals("MIT", openAPI.getInfo().getLicense().getName());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("Thiago Ferreira", openAPI.getInfo().getContact().getName());
    }

    @Test
    @DisplayName("Should apply V2 API customizer successfully")
    void shouldApplyV2ApiCustomizerSuccessfully() {
        // Arrange
        OpenAPI openAPI = new OpenAPI();
        OpenApiCustomizer customizer = (OpenApiCustomizer) ReflectionTestUtils.invokeMethod(openApiConfig, "v2ApiCustomizer");

        // Act
        customizer.customise(openAPI);

        // Assert
        assertNotNull(openAPI.getInfo());
        assertEquals("API Core - V2", openAPI.getInfo().getTitle());
        assertEquals("v2.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getDescription());
        assertTrue(openAPI.getInfo().getDescription().contains("API RESTful para o sistema de gestão de restaurantes Food App - Versão 2"));
        assertTrue(openAPI.getInfo().getDescription().contains("JWT"));
        assertNotNull(openAPI.getInfo().getLicense());
        assertEquals("MIT", openAPI.getInfo().getLicense().getName());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("Thiago Ferreira", openAPI.getInfo().getContact().getName());
    }
}

