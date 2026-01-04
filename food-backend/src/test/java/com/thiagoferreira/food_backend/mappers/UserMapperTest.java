package com.thiagoferreira.food_backend.mappers;

import com.thiagoferreira.food_backend.domain.dto.AddressDTO;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.Address;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserMapper Tests")
class UserMapperTest {

    private UserMapper userMapper;

    private UserRequest userRequest;
    private User user;
    private UserUpdateRequest userUpdateRequest;
    private AddressDTO addressDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();

        addressDTO = new AddressDTO();
        addressDTO.setStreet("Rua Teste");
        addressDTO.setNumber("123");
        addressDTO.setCity("São Paulo");
        addressDTO.setZipCode("01234-567");

        address = new Address();
        address.setStreet("Rua Teste");
        address.setNumber("123");
        address.setCity("São Paulo");
        address.setZipCode("01234-567");

        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setLogin("testuser");
        userRequest.setPassword("password123");
        userRequest.setType(UserType.CUSTOMER);
        userRequest.setAddress(addressDTO);

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setPassword("hashedpassword");
        user.setType(UserType.CUSTOMER);
        user.setAddress(address);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdated(LocalDateTime.now());

        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("Updated Name");
        userUpdateRequest.setAddress(addressDTO);
    }

    @Test
    @DisplayName("Should convert UserRequest to User entity")
    void shouldConvertUserRequestToEntity() {
        // Act
        User result = userMapper.toEntity(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userRequest.getName(), result.getName());
        assertEquals(userRequest.getEmail(), result.getEmail());
        assertEquals(userRequest.getLogin(), result.getLogin());
        assertEquals(userRequest.getPassword(), result.getPassword());
        assertEquals(userRequest.getType(), result.getType());
        assertNotNull(result.getAddress());
        assertEquals(userRequest.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(userRequest.getAddress().getNumber(), result.getAddress().getNumber());
        assertEquals(userRequest.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(userRequest.getAddress().getZipCode(), result.getAddress().getZipCode());
    }

    @Test
    @DisplayName("Should return null when UserRequest is null")
    void shouldReturnNullWhenUserRequestIsNull() {
        // Act
        User result = userMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should convert UserRequest to User entity without address")
    void shouldConvertUserRequestToEntityWithoutAddress() {
        // Arrange
        userRequest.setAddress(null);

        // Act
        User result = userMapper.toEntity(userRequest);

        // Assert
        assertNotNull(result);
        assertEquals(userRequest.getName(), result.getName());
        assertNull(result.getAddress());
    }

    @Test
    @DisplayName("Should convert User entity to UserResponse")
    void shouldConvertUserEntityToResponse() {
        // Act
        UserResponse result = userMapper.toResponse(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getLogin(), result.getLogin());
        assertEquals(user.getType(), result.getType());
        assertEquals(user.getLastUpdated(), result.getLastUpdate());
        assertNotNull(result.getAddress());
        assertEquals(user.getAddress().getStreet(), result.getAddress().getStreet());
        assertEquals(user.getAddress().getNumber(), result.getAddress().getNumber());
        assertEquals(user.getAddress().getCity(), result.getAddress().getCity());
        assertEquals(user.getAddress().getZipCode(), result.getAddress().getZipCode());
    }

    @Test
    @DisplayName("Should return null when User entity is null")
    void shouldReturnNullWhenUserEntityIsNull() {
        // Act
        UserResponse result = userMapper.toResponse(null);

        // Assert
        assertNull(result);
    }

    @Test
    @DisplayName("Should convert User entity to UserResponse without address")
    void shouldConvertUserEntityToResponseWithoutAddress() {
        // Arrange
        user.setAddress(null);

        // Act
        UserResponse result = userMapper.toResponse(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertNull(result.getAddress());
    }

    @Test
    @DisplayName("Should update User entity from UserUpdateRequest")
    void shouldUpdateUserEntityFromDto() {
        // Act
        userMapper.updateEntityFromDto(userUpdateRequest, user);

        // Assert
        assertEquals(userUpdateRequest.getName(), user.getName());
        assertNotNull(user.getAddress());
        assertEquals(userUpdateRequest.getAddress().getStreet(), user.getAddress().getStreet());
        assertEquals(userUpdateRequest.getAddress().getNumber(), user.getAddress().getNumber());
        assertEquals(userUpdateRequest.getAddress().getCity(), user.getAddress().getCity());
        assertEquals(userUpdateRequest.getAddress().getZipCode(), user.getAddress().getZipCode());
    }

    @Test
    @DisplayName("Should not update when UserUpdateRequest is null")
    void shouldNotUpdateWhenUserUpdateRequestIsNull() {
        // Arrange
        String originalName = user.getName();
        Address originalAddress = user.getAddress();

        // Act
        userMapper.updateEntityFromDto(null, user);

        // Assert
        assertEquals(originalName, user.getName());
        assertEquals(originalAddress, user.getAddress());
    }

    @Test
    @DisplayName("Should not update when User entity is null")
    void shouldNotUpdateWhenUserEntityIsNull() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> userMapper.updateEntityFromDto(userUpdateRequest, null));
    }

    @Test
    @DisplayName("Should not update when both are null")
    void shouldNotUpdateWhenBothAreNull() {
        // Act & Assert - Should not throw exception
        assertDoesNotThrow(() -> userMapper.updateEntityFromDto(null, null));
    }

    @Test
    @DisplayName("Should update User entity from UserUpdateRequest without address")
    void shouldUpdateUserEntityFromDtoWithoutAddress() {
        // Arrange
        userUpdateRequest.setAddress(null);

        // Act
        userMapper.updateEntityFromDto(userUpdateRequest, user);

        // Assert
        assertEquals(userUpdateRequest.getName(), user.getName());
        assertNull(user.getAddress());
    }

    @Test
    @DisplayName("Should handle AddressDTO to Address conversion")
    void shouldHandleAddressDtoToAddressConversion() {
        // Arrange
        UserRequest requestWithAddress = new UserRequest();
        requestWithAddress.setName("Test");
        requestWithAddress.setEmail("test@test.com");
        requestWithAddress.setLogin("test");
        requestWithAddress.setPassword("pass");
        requestWithAddress.setType(UserType.CUSTOMER);
        requestWithAddress.setAddress(addressDTO);

        // Act
        User result = userMapper.toEntity(requestWithAddress);

        // Assert
        assertNotNull(result.getAddress());
        assertEquals(addressDTO.getStreet(), result.getAddress().getStreet());
        assertEquals(addressDTO.getNumber(), result.getAddress().getNumber());
        assertEquals(addressDTO.getCity(), result.getAddress().getCity());
        assertEquals(addressDTO.getZipCode(), result.getAddress().getZipCode());
    }

    @Test
    @DisplayName("Should handle Address to AddressDTO conversion")
    void shouldHandleAddressToAddressDtoConversion() {
        // Act
        UserResponse result = userMapper.toResponse(user);

        // Assert
        assertNotNull(result.getAddress());
        assertEquals(address.getStreet(), result.getAddress().getStreet());
        assertEquals(address.getNumber(), result.getAddress().getNumber());
        assertEquals(address.getCity(), result.getAddress().getCity());
        assertEquals(address.getZipCode(), result.getAddress().getZipCode());
    }
}

