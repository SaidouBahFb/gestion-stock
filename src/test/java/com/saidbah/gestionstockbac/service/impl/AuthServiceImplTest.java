package com.saidbah.gestionstockbac.service.impl;

import com.saidbah.gestionstockbac.config.JwtService;
import com.saidbah.gestionstockbac.dto.request.AuthenticationRequest;
import com.saidbah.gestionstockbac.dto.request.RegisterRequest;
import com.saidbah.gestionstockbac.dto.response.AuthenticationResponse;
import com.saidbah.gestionstockbac.entity.Role;
import com.saidbah.gestionstockbac.entity.Status;
import com.saidbah.gestionstockbac.entity.User;
import com.saidbah.gestionstockbac.exception.EntityAlreadyExistsException;
import com.saidbah.gestionstockbac.exception.EntityInactiveException;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import com.saidbah.gestionstockbac.exception.StatusCode;
import com.saidbah.gestionstockbac.repository.UserRepository;
import com.saidbah.gestionstockbac.utils.Helpers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private LogService logService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_shouldThrowException_whenUserAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .firstname("user")
                .lastname("user")
                .email("user.user@example.com")
                .password("password")
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        EntityAlreadyExistsException exception = assertThrows(EntityAlreadyExistsException.class,
                () -> authService.register(request));
        assertEquals(StatusCode.USER_ALREADY_EXISTS.getMessage(), exception.getMessage());
        verify(logService).log(Helpers.LogLevel.ERROR, "@AuthServiceImpl-AuthenticationResponse", "L'utilisateur existe déjà");
    }

    @Test
    void register_shouldSaveUserAndReturnResponse_whenUserDoesNotExist() {
        // Arrange
        RegisterRequest request = RegisterRequest.builder()
                .firstname("user")
                .lastname("user")
                .email("user.user@gmail.com")
                .phone("620000000")
                .address("Conakry")
                .password("Passer123@")
                .roles(Collections.singletonList(Role.StockManager))
                .status(Status.ACTIVE)
                .photo("profile.jpg")
                .createdBy("admin")
                .companyId(1L)
                .build();

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");

        // Mocking save method of UserRepository
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L); // Mock setting ID after save
            return savedUser;
        });

        // Act
        AuthenticationResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("user", response.getFirstname());
        assertEquals("user", response.getLastname());
        assertEquals(Status.ACTIVE, response.getStatus());
        assertEquals(1L, response.getId());
        assertEquals("user.user@gmail.com", response.getEmail());
        assertEquals(Collections.singletonList("StockManager"), response.getRoles());
        verify(userRepository).save(any(User.class));
    }


    @Test
    void authenticate_shouldThrowException_whenUserNotFound() {
        // Arrange
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user.user@example.com")
                .password("password")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> authService.authenticate(request));
        assertEquals(StatusCode.USER_EMAIL_OR_PASSWORD_INVALID.getMessage(), exception.getMessage());
        verify(logService).log(Helpers.LogLevel.ERROR, "@AuthenticateService-authenticate", "Email ou de mot de passe incorrect.");
    }

    @Test
    void authenticate_shouldThrowException_whenPasswordDoesNotMatch() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user.user@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .email("user.user@example.com")
                .password("differentPassword")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> authService.authenticate(request));
        assertEquals(StatusCode.USER_EMAIL_OR_PASSWORD_INVALID.getMessage(), exception.getMessage());
        verify(logService).log(Helpers.LogLevel.ERROR, "@AuthenticateService-authenticate", "Email ou de mot de passe incorrect.");
    }

    @Test
    void authenticate_shouldThrowException_whenUserIsInactive() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user.user@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .email("user.user@example.com")
                .password("password")
                .status(Status.INACTIVE)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

        EntityInactiveException exception = assertThrows(EntityInactiveException.class,
                () -> authService.authenticate(request));
        assertEquals(StatusCode.USER_INACTIVE.getMessage(), exception.getMessage());
        verify(logService).log(Helpers.LogLevel.ERROR, "@AuthenticateService-authenticate", "L'utilisateur bloqué(e).");
    }

    @Test
    void authenticate_shouldReturnResponse_whenCredentialsAreValid() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user.user@example.com")
                .password("password")
                .build();

        User user = User.builder()
                .id(1L)
                .firstname("user")
                .lastname("user")
                .email("user.user@example.com")
                .phone("123456789")
                .address("123 Street")
                .password("encodedPassword")
                .roles(Collections.singletonList(Role.Admin))
                .status(Status.ACTIVE)
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        AuthenticationResponse response = authService.authenticate(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals("user", response.getFirstname());
        assertEquals("user", response.getLastname());
        assertEquals("user.user@example.com", response.getEmail());
        assertEquals(Status.ACTIVE, response.getStatus());
    }
}
