package com.example.springbootprojectuni.controller;

import com.example.springbootprojectuni.config.SecurityConfig;
import com.example.springbootprojectuni.dto.LoginRequest;
import com.example.springbootprojectuni.dto.RegisterRequest;
import com.example.springbootprojectuni.mapper.UserMapper;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.service.AuthService;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private JwtUtil jwtUtil;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("POST /api/users/register - Success")
    void registerUser_Success() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("john@example.com");
        registerRequest.setPassword("password");
        registerRequest.setName("John Doe");
        registerRequest.setRole("USER");

        User userToSave = new User();
        userToSave.setEmail(registerRequest.getEmail());
        userToSave.setPassword(registerRequest.getPassword());
        userToSave.setRole(registerRequest.getRole());

        when(userMapper.toEntity(any(RegisterRequest.class))).thenReturn(userToSave);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedPass");
        savedUser.setRole("USER");

        when(authService.registerUser(any(User.class))).thenReturn(savedUser);


        when(jwtUtil.generateToken(
                eq(savedUser.getEmail()),
                eq(savedUser.getRole()),
                eq(savedUser.getId()))
        ).thenReturn("mockToken");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string("mockToken"));
    }

    @Test
    @DisplayName("POST /api/users/login - Success")
    void loginUser_Success() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password");

        when(authService.login(eq("john@example.com"), eq("password")))
                .thenReturn("mockLoginToken");


        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("mockLoginToken"));
    }

    @Test
    @DisplayName("POST /api/users/login - Invalid credentials")
    void loginUser_InvalidCredentials() throws Exception {

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("wrong@example.com");
        loginRequest.setPassword("wrong");

        when(authService.login(eq("wrong@example.com"), eq("wrong")))
                .thenThrow(new IllegalArgumentException("Invalid email or password"));

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is4xxClientError());
    }
}
