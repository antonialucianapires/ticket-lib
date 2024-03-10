package com.alps.infrastructure.lock;

import java.util.concurrent.locks.ReentrantLock;

import com.alps.core.lock.LockProvider;

public class ReentrantLockProvider implements LockProvider {

    private final ReentrantLock reentrantLock = new ReentrantLock();

    @Override
    public void lock() {
        reentrantLock.lock();
    }

    @Override
    public void unlock() {
        reentrantLock.unlock();
    }

    @Override
    public boolean isLocked() {
        return reentrantLock.isLocked();
    }
}