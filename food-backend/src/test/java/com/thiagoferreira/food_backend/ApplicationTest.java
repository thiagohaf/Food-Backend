package com.thiagoferreira.food_backend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.lang.annotation.Annotation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("Application Tests")
class ApplicationTest {

    @Test
    @DisplayName("Should load Spring context successfully")
    void shouldLoadSpringContext() {
        // This test verifies that the Spring application context loads without errors
        // If the context fails to load, this test will fail
        assertTrue(true, "Spring context loaded successfully");
    }

    @Test
    @DisplayName("Should have SpringBootApplication annotation")
    void shouldHaveSpringBootApplicationAnnotation() {
        // Arrange & Act
        Annotation[] annotations = Application.class.getAnnotations();
        
        // Assert
        boolean hasSpringBootApplication = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(SpringBootApplication.class)) {
                hasSpringBootApplication = true;
                break;
            }
        }
        assertTrue(hasSpringBootApplication, "Application class should be annotated with @SpringBootApplication");
    }

    @Test
    @DisplayName("Should have EnableJpaAuditing annotation")
    void shouldHaveEnableJpaAuditingAnnotation() {
        // Arrange & Act
        Annotation[] annotations = Application.class.getAnnotations();
        
        // Assert
        boolean hasEnableJpaAuditing = false;
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(EnableJpaAuditing.class)) {
                hasEnableJpaAuditing = true;
                break;
            }
        }
        assertTrue(hasEnableJpaAuditing, "Application class should be annotated with @EnableJpaAuditing");
    }

    @Test
    @DisplayName("Should have main method")
    void shouldHaveMainMethod() {
        // Arrange & Act
        try {
            java.lang.reflect.Method mainMethod = Application.class.getMethod("main", String[].class);
            
            // Assert
            assertNotNull(mainMethod, "Application class should have a main method");
            assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()), 
                    "main method should be static");
            assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()), 
                    "main method should be public");
            assertEquals(void.class, mainMethod.getReturnType(), 
                    "main method should return void");
        } catch (NoSuchMethodException e) {
            fail("Application class should have a main method");
        }
    }

    @Test
    @DisplayName("Should be a public class")
    void shouldBePublicClass() {
        // Arrange & Act
        int modifiers = Application.class.getModifiers();
        
        // Assert
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers), 
                "Application class should be public");
    }

    @Test
    @DisplayName("Should have correct package")
    void shouldHaveCorrectPackage() {
        // Arrange & Act
        Package packageName = Application.class.getPackage();
        
        // Assert
        assertNotNull(packageName, "Application class should have a package");
        assertEquals("com.thiagoferreira.food_backend", packageName.getName(), 
                "Application class should be in package com.thiagoferreira.food_backend");
    }

    @Test
    @DisplayName("Should call SpringApplication.run with correct parameters")
    void shouldCallSpringApplicationRun() throws Exception {
        // Arrange
        String[] args = new String[]{"--test"};
        
        // Act & Assert
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Mock the run method to return a ConfigurableApplicationContext
            org.springframework.context.ConfigurableApplicationContext mockContext = 
                    mock(org.springframework.context.ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(eq(Application.class), eq(args)))
                    .thenReturn(mockContext);
            
            // Invoke the main method using reflection
            java.lang.reflect.Method mainMethod = Application.class.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
            
            // Verify that SpringApplication.run was called with correct parameters
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(Application.class), eq(args)), times(1));
        }
    }

    @Test
    @DisplayName("Should call SpringApplication.run with empty args")
    void shouldCallSpringApplicationRunWithEmptyArgs() throws Exception {
        // Arrange
        String[] args = new String[]{};
        
        // Act & Assert
        try (MockedStatic<SpringApplication> mockedSpringApplication = mockStatic(SpringApplication.class)) {
            // Mock the run method to return a ConfigurableApplicationContext
            org.springframework.context.ConfigurableApplicationContext mockContext = 
                    mock(org.springframework.context.ConfigurableApplicationContext.class);
            mockedSpringApplication.when(() -> SpringApplication.run(eq(Application.class), eq(args)))
                    .thenReturn(mockContext);
            
            // Invoke the main method using reflection
            java.lang.reflect.Method mainMethod = Application.class.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
            
            // Verify that SpringApplication.run was called with correct parameters
            mockedSpringApplication.verify(() -> SpringApplication.run(eq(Application.class), eq(args)), times(1));
        }
    }
}

