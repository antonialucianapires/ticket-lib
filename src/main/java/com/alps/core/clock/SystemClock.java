package com.alps.core.clock;

import java.time.Duration;

import com.alps.core.lock.LockProvider;

public class SystemClock {

    private static long currentTimeMillis = System.currentTimeMillis();
    private final LockProvider lockProvider;

    public SystemClock(LockProvider lockProvider) {
        this.lockProvider = lockProvider;
    }

    public long currentTimeMillis() {
        lockProvider.lock();
        try {
            return currentTimeMillis;
        } finally {
            lockProvider.unlock();
        }
    }

    public void advance(Duration duration) {
        lockProvider.lock();
        try {
            currentTimeMillis += duration.toMillis();
        } finally {
            lockProvider.unlock();
        }
    }

    public void reset() {
        lockProvider.lock();
        try {
            currentTimeMillis = System.currentTimeMillis();
        } finally {
            lockProvider.unlock();
        }
    }
}
