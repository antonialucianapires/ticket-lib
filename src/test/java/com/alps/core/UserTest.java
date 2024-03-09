package com.alps.core;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void assertNonNull() {
        assertThrows(NullPointerException.class, () -> {
            new User(null, "John Doe", "john.doe@example.com");
        });

        assertThrows(NullPointerException.class, () -> {
            new User("1", null, "john.doe@example.com");
        });

        assertThrows(NullPointerException.class, () -> {
            new User("1", "John Doe", null);
        });
    }

    @Test
    void shouldCreateUserWithValidDetails() {
        User user = new User("1", "John Doe", "john.doe@example.com");
        assertEquals("1", user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("john.doe@example.com", user.getEmail());
    }

    @Test
    void shouldUpdateUserName() {
        User user = new User("1", "John Doe", "john.doe@example.com");
        User updatedUser = user.withName("Jane Doe");
        assertEquals("Jane Doe", updatedUser.getName());
        assertNotEquals(user.getName(), updatedUser.getName());
    }

    @Test
    void shouldUpdateUserEmail() {
        User user = new User("1", "John Doe", "john.doe@example.com");
        User updatedUser = user.withEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", updatedUser.getEmail());
        assertNotEquals(user.getEmail(), updatedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionForWithInvalidEmail() {
        User user = new User("1", "John Doe", "john.doe@example.com");
        assertThrows(IllegalArgumentException.class, () -> {
            user.withEmail("invalid email");
        });

    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("1", "John Doe", "invalid email");
        });
    }

    @Test
    void shouldNotThrowExceptionForValidEmail() {
        assertDoesNotThrow(() -> {
            new User("1", "John Doe", "john.doe@example.com");
        });
    }
}
