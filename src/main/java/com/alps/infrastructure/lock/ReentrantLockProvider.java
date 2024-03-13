package com.alps.infrastructure.lock;

import java.util.concurrent.locks.ReentrantLock;

import com.alps.core.lock.LockProvider;

/**
 * An implementation of {@link LockProvider} using {@link ReentrantLock}.
 * This implementation provides a mutual exclusion lock with the same basic
 * behavior and semantics as the implicit monitor lock accessed using
 * {@code synchronized} methods and statements, but with extended capabilities.
 */
public class ReentrantLockProvider implements LockProvider {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    /**
     * Acquires the reentrant lock.
     * 
     * Blocks until the lock is available.
     */
    @Override
    public void lock() {
        reentrantLock.lock();
    }

    /**
     * Releases the reentrant lock.
     * 
     * If the current thread holds this lock, then the hold count is decremented.
     * If the hold count is now zero, the lock is released.
     */
    @Override
    public void unlock() {
        reentrantLock.unlock();
    }

    /**
     * Checks if the reentrant lock is currently acquired by any thread.
     * 
     * @return true if any thread holds this lock, false otherwise.
     */
    @Override
    public boolean isLocked() {
        return reentrantLock.isLocked();
    }
}