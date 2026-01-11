package com.thiagoferreira.food_backend.controllers;

import com.thiagoferreira.food_backend.domain.dto.AddressDTO;
import com.thiagoferreira.food_backend.domain.dto.PasswordChangeRequest;
import com.thiagoferreira.food_backend.domain.dto.UserRequest;
import com.thiagoferreira.food_backend.domain.dto.UserResponse;
import com.thiagoferreira.food_backend.domain.dto.UserUpdateRequest;
import com.thiagoferreira.food_backend.domain.entities.Address;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.domain.enums.UserType;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.mappers.UserMapper;
import com.thiagoferreira.food_backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserControllerV2 Tests")
class UserControllerV2Test {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserControllerV2 userControllerV2;

    private User user;
    private UserRequest userRequest;
    private UserResponse userResponse;
    private UserUpdateRequest userUpdateRequest;
    private AddressDTO addressDTO;
    private Address address;

    @BeforeEach
    void setUp() {
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

        user = new User();
        user.setId(1L);
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setPassword("hashedpassword");
        user.setType(UserType.CUSTOMER);
        user.setAddress(address);
        user.setLastUpdated(LocalDateTime.now());

        userRequest = new UserRequest();
        userRequest.setName("Test User");
        userRequest.setEmail("test@example.com");
        userRequest.setLogin("testuser");
        userRequest.setPassword("password123");
        userRequest.setType(UserType.CUSTOMER);
        userRequest.setAddress(addressDTO);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("Test User");
        userResponse.setEmail("test@example.com");
        userResponse.setLogin("testuser");
        userResponse.setType(UserType.CUSTOMER);
        userResponse.setAddress(addressDTO);
        userResponse.setLastUpdate(LocalDateTime.now());

        userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setName("Updated Name");
        userUpdateRequest.setAddress(addressDTO);
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(user);
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userControllerV2.create(userRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse.getId(), response.getBody().getId());
        verify(userMapper, times(1)).toEntity(userRequest);
        verify(userService, times(1)).createUser(user);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should find all users successfully")
    void shouldFindAllUsersSuccessfully() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        List<User> users = Arrays.asList(user, user2);
        UserResponse userResponse2 = new UserResponse();
        userResponse2.setId(2L);
        userResponse2.setName("User 2");

        when(userService.findUsers()).thenReturn(users);
        when(userMapper.toResponse(user)).thenReturn(userResponse);
        when(userMapper.toResponse(user2)).thenReturn(userResponse2);

        // Act
        ResponseEntity<List<UserResponse>> response = userControllerV2.findUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findUsers();
    }

    @Test
    @DisplayName("Should search users by name successfully")
    void shouldSearchUsersByNameSuccessfully() {
        // Arrange
        List<User> users = Arrays.asList(user);
        when(userService.searchByName("Test")).thenReturn(users);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<List<UserResponse>> response = userControllerV2.searchByName("Test");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(userService, times(1)).searchByName("Test");
    }

    @Test
    @DisplayName("Should search user by login successfully")
    void shouldSearchUserByLoginSuccessfully() {
        // Arrange
        when(userService.findByLogin("testuser")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userControllerV2.searchByLogin("testuser");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse.getLogin(), response.getBody().getLogin());
        verify(userService, times(1)).findByLogin("testuser");
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by login")
    void shouldThrowExceptionWhenUserNotFoundByLogin() {
        // Arrange
        when(userService.findByLogin("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userControllerV2.searchByLogin("nonexistent")
        );
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userService, times(1)).findByLogin("nonexistent");
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Should search user by email successfully")
    void shouldSearchUserByEmailSuccessfully() {
        // Arrange
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userControllerV2.searchByEmail("test@example.com");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse.getEmail(), response.getBody().getEmail());
        verify(userService, times(1)).findByEmail("test@example.com");
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by email")
    void shouldThrowExceptionWhenUserNotFoundByEmail() {
        // Arrange
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userControllerV2.searchByEmail("nonexistent@example.com")
        );
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(userService, times(1)).findByEmail("nonexistent@example.com");
        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Should search user by id successfully")
    void shouldSearchUserByIdSuccessfully() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        // Act
        ResponseEntity<UserResponse> response = userControllerV2.searchById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userResponse.getId(), response.getBody().getId());
        verify(userService, times(1)).findById(1L);
        verify(userMapper, times(1)).toResponse(user);
    }

    @Test
    @DisplayName("Should update user info successfully")
    void shouldUpdateUserInfoSuccessfully() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setName("Updated Name");
        updatedUser.setAddress(address);
        UserResponse updatedResponse = new UserResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Updated Name");

        when(userService.findById(1L)).thenReturn(user);
        doNothing().when(userMapper).updateEntityFromDto(any(UserUpdateRequest.class), any(User.class));
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);
        when(userMapper.toResponse(updatedUser)).thenReturn(updatedResponse);

        // Act
        ResponseEntity<UserResponse> response = userControllerV2.updateInfo(1L, userUpdateRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Name", response.getBody().getName());
        verify(userService, times(1)).findById(1L);
        verify(userMapper, times(1)).updateEntityFromDto(userUpdateRequest, user);
        verify(userService, times(1)).updateUser(eq(1L), any(User.class));
        verify(userMapper, times(1)).toResponse(updatedUser);
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Arrange
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();
        passwordChangeRequest.setCurrentPassword("oldpassword");
        passwordChangeRequest.setNewPassword("newpassword");
        doNothing().when(userService).changePassword(eq(1L), anyString(), anyString());

        // Act
        ResponseEntity<Void> response = userControllerV2.changePassword(1L, passwordChangeRequest);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).changePassword(
                eq(1L),
                eq("oldpassword"),
                eq("newpassword")
        );
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<Void> response = userControllerV2.delete(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(userService, times(1)).deleteUser(1L);
    }
}
