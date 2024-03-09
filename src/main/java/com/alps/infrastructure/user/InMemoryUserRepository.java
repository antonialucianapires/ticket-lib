package com.alps.infrastructure.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alps.core.user.User;
import com.alps.core.user.UserRepository;

public class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User findById(String id) {
        return users.get(id);
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(String id) {
        users.remove(id);
    }
}
