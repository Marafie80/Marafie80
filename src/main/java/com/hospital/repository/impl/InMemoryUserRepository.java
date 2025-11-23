package com.hospital.repository.impl;

import com.hospital.model.User;
import com.hospital.repository.CrudRepository;

import java.util.*;

public class InMemoryUserRepository implements CrudRepository<User, String> {
    private final Map<String, User> db = new HashMap<>();

    @Override
    public User save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId(UUID.randomUUID().toString());
        }
        db.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public Optional<User> findByUsername(String username) {
        return db.values().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }
}
