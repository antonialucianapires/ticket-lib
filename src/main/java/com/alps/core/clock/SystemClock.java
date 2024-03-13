package com.alps.core.clock;

import java.time.Duration;

import com.alps.core.lock.LockProvider;

public class SystemClock {

    private long currentTimeMillis;
    private final LockProvider lockProvider;

    public SystemClock(LockProvider lockProvider) {
        this.lockProvider = lockProvider;
        this.currentTimeMillis = System.currentTimeMillis();
    }

    /**
     * Returns the current time in milliseconds.
     * This method is thread-safe and uses the provided lockProvider for
     * synchronization.
     *
     * @return The current time in milliseconds as per this clock's state.
     */
    public long currentTimeMillis() {
        lockProvider.lock();
        try {
            return this.currentTimeMillis;
        } finally {
            lockProvider.unlock();
        }
    }

    /**
     * Advances the clock by the specified duration.
     * This method is thread-safe and uses the provided lockProvider for
     * synchronization.
     *
     * @param duration The duration to advance the clock by.
     */
    public void advance(Duration duration) {
        lockProvider.lock();
        try {
            this.currentTimeMillis += duration.toMillis();
        } finally {
            lockProvider.unlock();
        }
    }

    /**
     * Resets the clock to the current system time.
     * This method is thread-safe and uses the provided lockProvider for
     * synchronization.
     */
    public void reset() {
        lockProvider.lock();
        try {
            this.currentTimeMillis = System.currentTimeMillis();
        } finally {
            lockProvider.unlock();
        }
    }
}