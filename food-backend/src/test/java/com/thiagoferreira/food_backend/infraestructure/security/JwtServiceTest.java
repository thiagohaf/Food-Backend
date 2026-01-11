package com.thiagoferreira.food_backend.infraestructure.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private static final String SECRET_KEY = "defaultSecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForHS256Algorithm";
    private static final Long EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtService, "secret", SECRET_KEY);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
    }

    @Test
    @DisplayName("Should generate token successfully")
    void shouldGenerateTokenSuccessfully() {
        // Act
        String token = jwtService.generateToken("testuser", 1L);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        // Arrange
        String token = jwtService.generateToken("testuser", 1L);

        // Act
        String username = jwtService.extractUsername(token);

        // Assert
        assertEquals("testuser", username);
    }

    @Test
    @DisplayName("Should extract user ID from token (Long)")
    void shouldExtractUserIdFromTokenAsLong() {
        // Arrange
        Long userId = 123L;
        String token = jwtService.generateToken("testuser", userId);

        // Act
        Long extractedUserId = jwtService.extractUserId(token);

        // Assert
        assertNotNull(extractedUserId);
        assertEquals(userId, extractedUserId);
    }

    @Test
    @DisplayName("Should extract expiration date from token")
    void shouldExtractExpirationFromToken() {
        // Arrange
        String token = jwtService.generateToken("testuser", 1L);

        // Act
        Date expiration = jwtService.extractExpiration(token);

        // Assert
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Should extract claim from token")
    void shouldExtractClaimFromToken() {
        // Arrange
        String token = jwtService.generateToken("testuser", 1L);

        // Act
        String subject = jwtService.extractClaim(token, Claims::getSubject);

        // Assert
        assertEquals("testuser", subject);
    }

    @Test
    @DisplayName("Should return false when token is not expired")
    void shouldReturnFalseWhenTokenIsNotExpired() {
        // Arrange
        String token = jwtService.generateToken("testuser", 1L);

        // Act
        Boolean isExpired = jwtService.isTokenExpired(token);

        // Assert
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        // Arrange
        String username = "testuser";
        String token = jwtService.generateToken(username, 1L);

        // Act
        Boolean isValid = jwtService.validateToken(token, username);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should return false when token username does not match")
    void shouldReturnFalseWhenTokenUsernameDoesNotMatch() {
        // Arrange
        String token = jwtService.generateToken("testuser", 1L);

        // Act
        Boolean isValid = jwtService.validateToken(token, "differentuser");

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract user ID when userId is Integer")
    void shouldExtractUserIdWhenUserIdIsInteger() {
        // Arrange
        // Create a token with Integer userId manually
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 123); // Integer instead of Long
        
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claims(claims)
                .subject("testuser")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(signingKey)
                .compact();

        // Act
        Long extractedUserId = jwtService.extractUserId(token);

        // Assert
        assertNotNull(extractedUserId);
        assertEquals(123L, extractedUserId);
    }

    @Test
    @DisplayName("Should return null when userId is neither Integer nor Long")
    void shouldReturnNullWhenUserIdIsNeitherIntegerNorLong() {
        // Arrange
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", "invalid"); // String instead of Integer or Long
        
        SecretKey signingKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        String token = Jwts.builder()
                .claims(claims)
                .subject("testuser")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(signingKey)
                .compact();

        // Act
        Long extractedUserId = jwtService.extractUserId(token);

        // Assert
        assertNull(extractedUserId);
    }
}
