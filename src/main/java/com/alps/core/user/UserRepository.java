package com.alps.core.user;

public interface UserRepository {
    User findById(String id);
    User save(User user);
    void delete(String id);
}
