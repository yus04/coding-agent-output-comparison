package com.example.userapi.controller;

import com.example.userapi.entity.User;
import com.example.userapi.exception.InvalidIdException;
import com.example.userapi.exception.UserNotFoundException;
import com.example.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        // Validate ID (must be 0 or greater)
        if (id < 0) {
            throw new InvalidIdException("Invalid ID");
        }

        // Find user in database
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}