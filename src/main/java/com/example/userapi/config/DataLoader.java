package com.example.userapi.config;

import com.example.userapi.entity.User;
import com.example.userapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if the repository is empty
        if (userRepository.count() == 0) {
            userRepository.save(new User("Admin User", "admin@example.com", "ADMIN"));
            userRepository.save(new User("Regular User", "user@example.com", "USER"));
            userRepository.save(new User("Test User", "test@example.com", "USER"));
        }
    }
}