package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findByDeletedFalse(); // Fetch only non-deleted users
    }

    public User getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isDeleted()) {
            throw new RuntimeException("User is deleted");
        }
        return user;
    }

    public void softDeleteUser(Long id) {
        User user = getUserById(id);
        user.setDeleted(true); // Mark as deleted
        userRepository.save(user);
    }
}
