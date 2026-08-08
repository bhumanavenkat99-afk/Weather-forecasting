package com.weatherforecast.service;

import com.weatherforecast.dto.LoginRequest;
import com.weatherforecast.dto.RegisterRequest;
import com.weatherforecast.model.User;
import com.weatherforecast.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        authenticationManager = Mockito.mock(AuthenticationManager.class);
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager);
    }

    @Test
    void registerCreatesNewUser() {
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals("Registration successful.", authService.register(request).getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerRejectsDuplicateEmail() {
        when(userRepository.existsByEmail("dup@example.com")).thenReturn(true);
        RegisterRequest request = new RegisterRequest();
        request.setName("Dup");
        request.setEmail("dup@example.com");
        request.setPassword("password");

        assertEquals("Email is already registered.", authService.register(request).getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void loginReturnsSuccessForValidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("valid@example.com");
        request.setPassword("password");

        assertEquals("Login successful.", authService.login(request).getMessage());
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void loginReturnsErrorForInvalidCredentials() {
        LoginRequest request = new LoginRequest();
        request.setEmail("bad@example.com");
        request.setPassword("1234");

        doThrow(new BadCredentialsException("Bad credentials")).when(authenticationManager).authenticate(any());
        assertEquals("Invalid credentials. Please check email and password.", authService.login(request).getMessage());
    }
}
