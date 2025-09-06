package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")        // Load application-test.properties
@Transactional                // Rollback DB changes after each test
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setEmail("testuser@example.com");
        user.setPassword("testpassword");
        user.setRole("USER");

        User savedUser = authService.registerUser(user);

        assertNotNull(savedUser.getId());
        assertEquals("testuser@example.com", savedUser.getEmail());
        assertTrue(passwordEncoder.matches("testpassword", savedUser.getPassword()));
    }

    @Test
    void registerUser_UserAlreadyExists() {
        User user = new User();
        user.setEmail("existinguser@example.com");
        user.setPassword("password");
        user.setRole("USER");

        authService.registerUser(user);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.registerUser(user);
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }
}
