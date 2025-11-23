package com.hospital.service;

import com.hospital.exception.ValidationException;
import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.repository.impl.InMemoryUserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthService {
    private final InMemoryUserRepository userRepository;
    private User currentUser;

    public AuthService(InMemoryUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new ValidationException("Username and password are required");
        }

        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public User register(String username, String password, Role role) {
        if (username == null || username.isEmpty()) {
            throw new ValidationException("Username is required");
        }
        if (password == null || password.length() < 6) {
            throw new ValidationException("Password must be at least 6 characters");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new ValidationException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hashPassword(password));
        user.setRole(role);
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
