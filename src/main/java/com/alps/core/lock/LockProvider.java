package com.alps.core.lock;

public interface LockProvider {
    void lock();
    void unlock();
    boolean isLocked();
}
