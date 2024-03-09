package com.alps.infrastructure.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.user.User;

public class InMemoryUserRepositoryTest {

    private InMemoryUserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository = new InMemoryUserRepository();
    }

    @Test
    void shouldSaveAndFindUserById() {
        User user = new User("1", "John Doe", "john.doe@example.com");
        userRepository.save(user);
        User foundUser = userRepository.findById("1");
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void shouldDeleteUser() {
        User user = new User("2", "Jane Doe", "jane.doe@example.com");
        userRepository.save(user);
        userRepository.delete("2");
        assertNull(userRepository.findById("2"));
    }

    @Test
    void shouldHandleConcurrentOperations() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<User> future1 = executorService.submit(() -> {
            User user = new User("3", "Concurrent User 1", "concurrent1@example.com");
            userRepository.save(user);
            return userRepository.findById("3");
        });

        Future<User> future2 = executorService.submit(() -> {
            User user = new User("4", "Concurrent User 2", "concurrent2@example.com");
            userRepository.save(user);
            return userRepository.findById("4");
        });

        User user1 = future1.get();
        User user2 = future2.get();

        assertNotNull(user1);
        assertNotNull(user2);
        assertEquals("Concurrent User 1", user1.getName());
        assertEquals("Concurrent User 2", user2.getName());

        executorService.shutdown();
    }

}
