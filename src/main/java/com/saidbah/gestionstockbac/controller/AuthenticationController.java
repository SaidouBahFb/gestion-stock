package com.saidbah.gestionstockbac.controller;

import com.saidbah.gestionstockbac.apiResponse.ApiResponse;
import com.saidbah.gestionstockbac.dto.request.AuthenticationRequest;
import com.saidbah.gestionstockbac.dto.request.RegisterRequest;
import com.saidbah.gestionstockbac.dto.response.AuthenticationResponse;
import com.saidbah.gestionstockbac.entity.User;
import com.saidbah.gestionstockbac.exception.StatusCode;
import com.saidbah.gestionstockbac.repository.UserRepository;
import com.saidbah.gestionstockbac.service.AuthService;
import com.saidbah.gestionstockbac.service.impl.LogService;
import com.saidbah.gestionstockbac.utils.Helpers;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthService authenticationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Path location = Paths.get("D:\\inventory-management\\users");
    private Path fileLocation;
    private final LogService logService;

    @PostMapping("/register")
    @Operation(summary = "Enregistrer un nouvel utilisateur", description = "Cet endpoint permet d'enregistrer un nouvel utilisateur avec les détails fournis.")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
            @RequestBody RegisterRequest request
    ) {
        var response = authenticationService.register(request);
        logService.log(Helpers.LogLevel.INFO, "@AuthenticationController-register", "L'utilisateur a été ajouté avec succès.");
        return ResponseEntity.ok().body(new ApiResponse<>(response, StatusCode.USER_REGISTER_SUCCESSFULLY.getCode(),
                StatusCode.USER_REGISTER_SUCCESSFULLY.getMessage()));
    }


    @PostMapping("/authenticate")
    @Operation(summary = "S'authentifier", description = "Cet endpoint permet de s'authentifier à l'application.")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        var response = authenticationService.authenticate(request);
        logService.log(Helpers.LogLevel.INFO, "@AuthenticationController-authenticate", "L'utilisateur connecté(e) avec succès.");
        return   ResponseEntity.ok().body(new ApiResponse<>(response, StatusCode.USER_AUTHENTICATED_SUCCESSFULLY.getCode(),
                StatusCode.USER_AUTHENTICATED_SUCCESSFULLY.getMessage()));
    }

    @PostMapping("/download-img/{userId}")
    public ResponseEntity<ApiResponse<String>> downloadImg(
            @RequestParam("photo") MultipartFile img,
            @PathVariable Long userId
    ) throws IOException {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = dateFormat.format(currentDate);

        String randomFileName = userId.toString() + "_" + UUID.randomUUID().toString()  + "_" + formattedDate + "_" + img.getOriginalFilename();

        fileLocation = this.location.resolve(randomFileName);

        Files.copy(img.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        user.setPhoto(fileLocation.toString());
        userRepository.save(user);
        logService.log(Helpers.LogLevel.INFO, "@AuthenticationController-download-img", "L'image de l'utilisateur a été enregistré avec succès");
        return ResponseEntity.ok().body(new ApiResponse<>(fileLocation.toString(), 200, "Photo téléchargée avec succès" ));
    }
}
