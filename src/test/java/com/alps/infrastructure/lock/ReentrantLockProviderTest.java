package com.alps.infrastructure.lock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.lock.LockProvider;

public class ReentrantLockProviderTest {

    LockProvider lockProvider;

    @BeforeEach
    void setup() {
        lockProvider = new ReentrantLockProvider();
    }

    @Test
    void shouldLockAndUnlock() {
        lockProvider.lock();
        assertTrue(lockProvider.isLocked());

        lockProvider.unlock();
        assertFalse(lockProvider.isLocked());
    }

    @Test
    void shouldUnlockWhenLockIsNotLocked() {
        lockProvider.unlock();
        assertFalse(lockProvider.isLocked());
    }

}
