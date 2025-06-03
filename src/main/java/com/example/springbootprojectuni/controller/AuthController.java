package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.dto.LoginRequest;
import com.example.springbootprojectuni.dto.RegisterRequest;
import com.example.springbootprojectuni.mapper.UserMapper;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.service.AuthService;
import com.example.springbootprojectuni.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    public AuthService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    public JwtUtil jwtUtil;

    @Operation(summary = "Register a new user", description = "Registers a new user and returns a JWT token.")
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        User user = userMapper.toEntity(registerRequest);
        User savedUser = userService.registerUser(user);
        String token = jwtUtil.generateToken(savedUser.getEmail(), savedUser.getRole(), savedUser.getId());

        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @Operation(summary = "Authenticate a user", description = "Logs in an existing user and returns a JWT token.")
    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        String token = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}