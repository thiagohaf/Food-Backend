package com.thiagoferreira.food_backend.services;

import com.thiagoferreira.food_backend.domain.entities.Address;
import com.thiagoferreira.food_backend.domain.entities.User;
import com.thiagoferreira.food_backend.domain.enums.ErrorMessages;
import com.thiagoferreira.food_backend.domain.enums.UserType;
import com.thiagoferreira.food_backend.exceptions.DomainValidationException;
import com.thiagoferreira.food_backend.exceptions.ResourceNotFoundException;
import com.thiagoferreira.food_backend.infraestructure.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Tests")
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Address address;

    @BeforeEach
    void setUp() {
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
        user.setPassword("password123");
        user.setType(UserType.CUSTOMER);
        user.setAddress(address);
        user.setCreatedAt(LocalDateTime.now());
        user.setLastUpdated(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Arrange
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.existsByLogin(anyString())).thenReturn(false);
        when(repository.save(any(User.class))).thenReturn(user);

        // Act
        User result = userService.createUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        assertNotEquals("password123", result.getPassword()); // Password should be hashed
        verify(repository, times(1)).existsByEmail(user.getEmail());
        verify(repository, times(1)).existsByLogin(user.getLogin());
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DomainValidationException when email already exists")
    void shouldThrowExceptionWhenEmailExists() {
        // Arrange
        when(repository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.createUser(user)
        );
        assertEquals(ErrorMessages.EMAIL_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(repository, times(1)).existsByEmail(user.getEmail());
        verify(repository, never()).existsByLogin(anyString());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw DomainValidationException when login already exists")
    void shouldThrowExceptionWhenLoginExists() {
        // Arrange
        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.existsByLogin(anyString())).thenReturn(true);

        // Act & Assert
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.createUser(user)
        );
        assertEquals(ErrorMessages.LOGIN_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(repository, times(1)).existsByEmail(user.getEmail());
        verify(repository, times(1)).existsByLogin(user.getLogin());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should find all users")
    void shouldFindAllUsers() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("User 2");
        List<User> users = Arrays.asList(user, user2);
        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.findUsers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find user by id successfully")
    void shouldFindUserByIdSuccessfully() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getEmail(), result.getEmail());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found by id")
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(999L)
        );
        assertTrue(exception.getMessage().contains("999"));
        verify(repository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should find user by login successfully")
    void shouldFindUserByLoginSuccessfully() {
        // Arrange
        when(repository.findByLogin("testuser")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByLogin("testuser");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getLogin(), result.get().getLogin());
        verify(repository, times(1)).findByLogin("testuser");
    }

    @Test
    @DisplayName("Should return empty when user not found by login")
    void shouldReturnEmptyWhenUserNotFoundByLogin() {
        // Arrange
        when(repository.findByLogin("nonexistent")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByLogin("nonexistent");

        // Assert
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByLogin("nonexistent");
    }

    @Test
    @DisplayName("Should find user by email successfully")
    void shouldFindUserByEmailSuccessfully() {
        // Arrange
        when(repository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        Optional<User> result = userService.findByEmail("test@example.com");

        // Assert
        assertTrue(result.isPresent());
        assertEquals(user.getEmail(), result.get().getEmail());
        verify(repository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        // Arrange
        when(repository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Assert
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("Should authenticate user successfully")
    void shouldAuthenticateUserSuccessfully() {
        // Arrange
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("password123", org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPassword(hashedPassword);
        when(repository.findByLogin("testuser")).thenReturn(Optional.of(user));

        // Act
        User result = userService.authenticate("testuser", "password123");

        // Assert
        assertNotNull(result);
        assertEquals(user.getLogin(), result.getLogin());
        verify(repository, times(1)).findByLogin("testuser");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when login not found during authentication")
    void shouldThrowExceptionWhenLoginNotFound() {
        // Arrange
        when(repository.findByLogin("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.authenticate("nonexistent", "password123")
        );
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(repository, times(1)).findByLogin("nonexistent");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when password is incorrect")
    void shouldThrowExceptionWhenPasswordIncorrect() {
        // Arrange
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw("correctpassword", org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPassword(hashedPassword);
        when(repository.findByLogin("testuser")).thenReturn(Optional.of(user));

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.authenticate("testuser", "wrongpassword")
        );
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(repository, times(1)).findByLogin("testuser");
    }

    @Test
    @DisplayName("Should search users by name")
    void shouldSearchUsersByName() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Test User 2");
        List<User> users = Arrays.asList(user, user2);
        when(repository.findByNameContainingIgnoreCaseOrderByNameAsc("Test")).thenReturn(users);

        // Act
        List<User> result = userService.searchByName("Test");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findByNameContainingIgnoreCaseOrderByNameAsc("Test");
        verify(repository, never()).findAll();
    }

    @Test
    @DisplayName("Should return all users when name is null")
    void shouldReturnAllUsersWhenNameIsNull() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Another User");
        List<User> users = Arrays.asList(user, user2);
        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.searchByName(null);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
        verify(repository, never()).findByNameContainingIgnoreCaseOrderByNameAsc(anyString());
    }

    @Test
    @DisplayName("Should return all users when name is empty")
    void shouldReturnAllUsersWhenNameIsEmpty() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Another User");
        List<User> users = Arrays.asList(user, user2);
        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.searchByName("");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
        verify(repository, never()).findByNameContainingIgnoreCaseOrderByNameAsc(anyString());
    }

    @Test
    @DisplayName("Should return all users when name is only whitespace")
    void shouldReturnAllUsersWhenNameIsOnlyWhitespace() {
        // Arrange
        User user2 = new User();
        user2.setId(2L);
        user2.setName("Another User");
        List<User> users = Arrays.asList(user, user2);
        when(repository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.searchByName("   ");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
        verify(repository, never()).findByNameContainingIgnoreCaseOrderByNameAsc(anyString());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        updatedUser.setAddress(address);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateUser(1L, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertNotNull(result.getLastUpdated());
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistentUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setName("Updated Name");
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(999L, updatedUser)
        );
        assertTrue(exception.getMessage().contains("999"));
        verify(repository, times(1)).findById(999L);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should change password successfully")
    void shouldChangePasswordSuccessfully() {
        // Arrange
        String currentPassword = "oldpassword";
        String newPassword = "newpassword";
        String hashedCurrentPassword = org.mindrot.jbcrypt.BCrypt.hashpw(currentPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPassword(hashedCurrentPassword);
        when(repository.findById(1L)).thenReturn(Optional.of(user));
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        assertDoesNotThrow(() -> userService.changePassword(1L, currentPassword, newPassword));

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(User.class));
        // Verify that the password was hashed
        assertNotEquals(newPassword, user.getPassword());
    }

    @Test
    @DisplayName("Should throw DomainValidationException when current password is incorrect")
    void shouldThrowExceptionWhenCurrentPasswordIncorrect() {
        // Arrange
        String currentPassword = "wrongpassword";
        String newPassword = "newpassword";
        String hashedStoredPassword = org.mindrot.jbcrypt.BCrypt.hashpw("correctpassword", org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPassword(hashedStoredPassword);
        when(repository.findById(1L)).thenReturn(Optional.of(user));

        // Act & Assert
        DomainValidationException exception = assertThrows(
                DomainValidationException.class,
                () -> userService.changePassword(1L, currentPassword, newPassword)
        );
        assertEquals(ErrorMessages.PASSWORD_MISMATCH.getMessage(), exception.getMessage());
        verify(repository, times(1)).findById(1L);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when changing password for non-existent user")
    void shouldThrowExceptionWhenChangingPasswordForNonExistentUser() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.changePassword(999L, "old", "new")
        );
        assertTrue(exception.getMessage().contains("999"));
        verify(repository, times(1)).findById(999L);
        verify(repository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should delete user successfully")
    void shouldDeleteUserSuccessfully() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        // Act
        assertDoesNotThrow(() -> userService.deleteUser(1L));

        // Assert
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistentUser() {
        // Arrange
        when(repository.existsById(999L)).thenReturn(false);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.deleteUser(999L)
        );
        assertEquals(ErrorMessages.USER_NOT_FOUND.getMessage(), exception.getMessage());
        verify(repository, times(1)).existsById(999L);
        verify(repository, never()).deleteById(anyLong());
    }
}

