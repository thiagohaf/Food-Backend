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
    @DisplayName("Should create OpenAPI bean successfully")
    void shouldCreateOpenApiBean() {
        // Act
        OpenAPI openAPI = openApiConfig.springShopOpenAPI();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("Food API", openAPI.getInfo().getTitle());
        assertEquals("v1.0", openAPI.getInfo().getVersion());
        assertNotNull(openAPI.getInfo().getLicense());
        assertEquals("MIT", openAPI.getInfo().getLicense().getName());
        assertNotNull(openAPI.getInfo().getContact());
        assertEquals("Thiago Ferreira", openAPI.getInfo().getContact().getName());
        assertNotNull(openAPI.getExternalDocs());
    }

    @Test
    @DisplayName("Should create GroupedOpenApi bean successfully")
    void shouldCreateGroupedOpenApiBean() {
        // Act
        GroupedOpenApi groupedOpenApi = openApiConfig.publicApi();

        // Assert
        assertNotNull(groupedOpenApi);
        assertEquals("v1", groupedOpenApi.getGroup());
    }
}

