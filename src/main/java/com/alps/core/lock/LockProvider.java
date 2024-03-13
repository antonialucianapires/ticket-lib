package com.alps.core.lock;

/**
 * Provides a locking mechanism interface that can be used to control access to
 * a resource
 * by multiple threads. Lock implementations ensure that only one thread can
 * hold the lock at
 * a time to prevent concurrent modification issues.
 */
public interface LockProvider {

    /**
     * Acquires the lock.
     * 
     * If the lock is not available, the current thread becomes disabled for thread
     * scheduling
     * purposes and lies dormant until the lock has been acquired.
     */
    void lock();

    /**
     * Releases the lock.
     * 
     * If the current thread holds this lock, then the lock is released. If the
     * current thread
     * does not hold the lock, then {@link IllegalMonitorStateException} is thrown.
     */
    void unlock();

    /**
     * Checks if the lock is currently acquired by any thread.
     * 
     * @return true if the lock is acquired by any thread, false otherwise.
     */
    boolean isLocked();
}
