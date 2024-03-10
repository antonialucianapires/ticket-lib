package com.alps.core.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alps.core.lock.LockProvider;
import com.alps.infrastructure.lock.ReentrantLockProvider;

public class SystemClockTest {

    LockProvider lockProvider;
    SystemClock systemClock;

    @BeforeEach
    void setup() {
        lockProvider = new ReentrantLockProvider();
        systemClock = new SystemClock(lockProvider);
        systemClock.reset();
    }

    @Test
    void shouldReturnCurrentTimeMillis() {
        long expectedTimeMillis = System.currentTimeMillis();
        long actualTimeMillis = systemClock.currentTimeMillis();
        long toleranceMillis = 1000;
        assertTrue(Math.abs(expectedTimeMillis - actualTimeMillis) <= toleranceMillis);
    }

    @Test
    void shouldAdvanceTimeByDuration() {
        long initialTimeMillis = systemClock.currentTimeMillis();
        Duration duration = Duration.ofSeconds(5);
        systemClock.advance(duration);
        long expectedTimeMillis = initialTimeMillis + duration.toMillis();
        long actualTimeMillis = systemClock.currentTimeMillis();
        assertEquals(expectedTimeMillis, actualTimeMillis);
    }

    @Test
    void shouldNotDecreaseTime() {
        Duration duration = Duration.ofSeconds(5);
        systemClock.advance(duration);
        long expectedTimeMillis = systemClock.currentTimeMillis();
        systemClock.advance(Duration.ofSeconds(-10)); 
        long actualTimeMillis = systemClock.currentTimeMillis();
        long toleranceMillis = 1000000;
        assertTrue(Math.abs(expectedTimeMillis - actualTimeMillis) <= toleranceMillis);
    }
}
