package com.saidbah.gestionstockbac.service.Impl;

import com.saidbah.gestionstockbac.config.JwtService;
import com.saidbah.gestionstockbac.dto.request.AuthenticationRequest;
import com.saidbah.gestionstockbac.dto.request.RegisterRequest;
import com.saidbah.gestionstockbac.dto.response.AuthenticationResponse;
import com.saidbah.gestionstockbac.entity.Status;
import com.saidbah.gestionstockbac.entity.User;
import com.saidbah.gestionstockbac.exception.EntityAlreadyExistsException;
import com.saidbah.gestionstockbac.exception.EntityInactiveException;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import com.saidbah.gestionstockbac.exception.StatusCode;
import com.saidbah.gestionstockbac.repository.UserRepository;
import com.saidbah.gestionstockbac.service.AuthService;
import com.saidbah.gestionstockbac.utils.Helpers;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final LogService logService;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.existsByEmail(request.getEmail())) {
            logService.log(Helpers.LogLevel.ERROR, "@AuthServiceImpl-AuthenticationResponse", "L'utilisateur existe déjà");
            throw new EntityAlreadyExistsException(
                    StatusCode.USER_ALREADY_EXISTS.getMessage(),
                    StatusCode.USER_ALREADY_EXISTS.getCode()
            );
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .status(Status.valueOf("ACTIVE"))
                .build();
        repository.save(user);

        return AuthenticationResponse.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .status(user.getStatus())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = repository.findByEmail(request.getEmail()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logService.log(Helpers.LogLevel.ERROR, "@AuthenticateService-authenticate", "Email ou de mot de passe incorrect.");
            throw new EntityNotFoundException(
                    StatusCode.USER_EMAIL_OR_PASSWORD_INVALID.getMessage(),
                    StatusCode.USER_EMAIL_OR_PASSWORD_INVALID.getCode()
            );

        }
        if (user.getStatus() == Status.INACTIVE) {
            logService.log(Helpers.LogLevel.ERROR, "@AuthenticateService-authenticate", "L'utilisateur bloqué(e).");
            throw new EntityInactiveException(
                    StatusCode.USER_INACTIVE.getMessage(),
                    StatusCode.USER_INACTIVE.getCode()
            );
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .status(user.getStatus())
                .build();
    }
}
