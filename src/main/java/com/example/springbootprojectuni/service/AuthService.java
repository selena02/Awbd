package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.exception.UserAlreadyExistsException;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User registerUser(User user) {
        logger.info("Attempting to register user with email: {}", user.getEmail());

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("Registration failed: user with email {} already exists", user.getEmail());
            throw new UserAlreadyExistsException("User with email " + user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);

        logger.info("User successfully registered with email: {}", savedUser.getEmail());
        return savedUser;
    }

    public String login(String email, String password) {
        logger.info("Login attempt for email: {}", email);

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            logger.warn("Login failed: user with email {} not found", email);
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(password, user.getPassword())) {
            logger.warn("Login failed: incorrect password for email {}", email);
            throw new IllegalArgumentException("Invalid email or password");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole(), user.getId());
        logger.info("Login successful for email: {}. Token generated.", email);

        return token;
    }
}
