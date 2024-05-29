package com.saidbah.gestionstockbac.service;


import com.saidbah.gestionstockbac.dto.request.AuthenticationRequest;
import com.saidbah.gestionstockbac.dto.request.RegisterRequest;
import com.saidbah.gestionstockbac.dto.response.AuthenticationResponse;

public interface AuthService {
    AuthenticationResponse register(RegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
}
