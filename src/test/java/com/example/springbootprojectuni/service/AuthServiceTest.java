package com.example.springbootprojectuni.service;

import com.example.springbootprojectuni.exception.UserAlreadyExistsException;
import com.example.springbootprojectuni.model.User;
import com.example.springbootprojectuni.repository.UserRepository;
import com.example.springbootprojectuni.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("registerUser - Success")
    void registerUser_Success() {

        User user = new User();
        user.setEmail("newuser@example.com");
        user.setPassword("plainPass");


        when(userRepository.findByEmail("newuser@example.com"))
                .thenReturn(Optional.empty());


        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");


        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail("newuser@example.com");
        savedUser.setPassword("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = authService.registerUser(user);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("newuser@example.com", result.getEmail());
        assertEquals("encodedPass", result.getPassword());

        verify(userRepository, times(1)).findByEmail("newuser@example.com");
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("plainPass");
    }

    @Test
    @DisplayName("registerUser - UserAlreadyExistsException")
    void registerUser_UserAlreadyExists() {

        User user = new User();
        user.setEmail("existing@example.com");

        when(userRepository.findByEmail("existing@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(UserAlreadyExistsException.class, () -> {
            authService.registerUser(user);
        });

        verify(userRepository, times(1)).findByEmail("existing@example.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("login - Success")
    void login_Success() {

        String email = "john@example.com";
        String rawPassword = "1234";
        String encodedPassword = "encodedPass";

        User user = new User();
        user.setId(10L);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword))
                .thenReturn(true);
        when(jwtUtil.generateToken(email, "USER", 10L))
                .thenReturn("mockJwtToken");


        String token = authService.login(email, rawPassword);


        assertEquals("mockJwtToken", token);
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, times(1)).generateToken(email, "USER", 10L);
    }

    @Test
    @DisplayName("login - Invalid Email")
    void login_InvalidEmail() {

        String email = "wrong@example.com";
        String password = "test";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(email, password);
        });

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("login - Invalid Password")
    void login_InvalidPassword() {
        String email = "john@example.com";
        String rawPassword = "wrong";
        String encodedPassword = "encodedPass";

        User user = new User();
        user.setId(10L);
        user.setEmail(email);
        user.setPassword(encodedPassword);
        user.setRole("USER");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            authService.login(email, rawPassword);
        });

        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(rawPassword, encodedPassword);
        verify(jwtUtil, never()).generateToken(anyString(), anyString(), anyLong());
    }
}
