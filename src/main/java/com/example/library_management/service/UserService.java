package com.example.library_management.service;

import com.example.library_management.model.User;
import com.example.library_management.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Add or update user
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Delete user by Id
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by Id
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
