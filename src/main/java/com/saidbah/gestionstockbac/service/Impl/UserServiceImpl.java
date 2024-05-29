package com.saidbah.gestionstockbac.service.Impl;

import com.saidbah.gestionstockbac.dto.request.UserRequest;
import com.saidbah.gestionstockbac.dto.response.PartialCompanyResponse;
import com.saidbah.gestionstockbac.dto.response.FullCompanyResponse;
import com.saidbah.gestionstockbac.dto.response.UserResponse;
import com.saidbah.gestionstockbac.entity.Company;
import com.saidbah.gestionstockbac.entity.Status;
import com.saidbah.gestionstockbac.entity.User;
import com.saidbah.gestionstockbac.exception.EntityAlreadyExistsException;
import com.saidbah.gestionstockbac.exception.StatusCode;
import com.saidbah.gestionstockbac.repository.CompanyRepository;
import com.saidbah.gestionstockbac.repository.UserRepository;
import com.saidbah.gestionstockbac.service.UserService;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        List<UserResponse> responses = users.stream()
                .map(user -> {
                    Company company = user.getCompany();
                    PartialCompanyResponse response = PartialCompanyResponse
                            .builder()
                            .id(company != null ? company.getId() : null)
                            .name(company !=null ? company.getName(): null)
                            .build();
                    return UserResponse.builder()
                            .id(user.getId())
                            .firstname(user.getFirstname())
                            .lastname(user.getLastname())
                            .email(user.getEmail())
                            .photo(user.getPhoto())
                            .phone(user.getPhone())
                            .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                            .status(user.getStatus())
                            .address(user.getAddress())
                            .photo(user.getPhoto())
                            .createdBy(user.getCreatedBy())
                            .companyResponse(response)
                            .build();
                })
                .collect(Collectors.toList());

        return responses;
    }

    @Override
    public UserResponse create(UserRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException(
                    StatusCode.USER_ALREADY_EXISTS.getMessage(),
                    StatusCode.USER_ALREADY_EXISTS.getCode()
            );
        }

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        StatusCode.COMPANY_NOT_FOUND.getMessage(),
                        StatusCode.COMPANY_NOT_FOUND.getCode())
                );

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .address(request.getAddress())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .status(Status.ACTIVE)
                .createdBy(currentUserEmail)
                .company(company)
                .build();

        FullCompanyResponse companyResponse = FullCompanyResponse.builder()
                .id(company != null ? company.getId() : null)
                .name(company !=null ? company.getName(): null)
                .email(company != null ? company.getEmail() : null)
                .build();

        userRepository.save(user);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .address(user.getAddress())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .status(user.getStatus())
                .createdBy(user.getCreatedBy())
                .company(companyResponse)
                .build();
    }

    @Override
    public List<UserResponse> getUserByCompany(Long companyId) {
        Company company = companyRepository.findById(companyId).orElseThrow(
                () -> new EntityNotFoundException(
                        StatusCode.COMPANY_NOT_FOUND.getMessage(),
                        StatusCode.COMPANY_NOT_FOUND.getCode()
                )
        );
        List<User> users = userRepository.findByCompany(company);

        List<UserResponse> userResponses = users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstname(user.getFirstname())
                        .lastname(user.getLastname())
                        .email(user.getEmail())
                        .photo(user.getPhoto())
                        .phone(user.getPhone())
                        .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                        .status(user.getStatus())
                        .address(user.getAddress())
                        .createdBy(user.getCreatedBy())
                        .build())
                .collect(Collectors.toList());

        return userResponses;
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    StatusCode.USER_NOT_FOUND.getMessage(),
                    StatusCode.USER_NOT_FOUND.getCode()
            );
        }

        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (user != null && !user.getId().equals(id)) {
            throw new EntityAlreadyExistsException(
                    StatusCode.USER_ALREADY_EXISTS.getMessage(),
                    StatusCode.USER_ALREADY_EXISTS.getCode()
            );
        }
        Company company = companyRepository.findById(request.getCompanyId())
                        .orElseThrow(() -> new EntityNotFoundException
                                (
                                    StatusCode.COMPANY_NOT_FOUND.getMessage(),
                                    StatusCode.COMPANY_NOT_FOUND.getCode()
                                )
                        );

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setRoles(request.getRoles());
        user.setCompany(company);

        userRepository.save(user);

        PartialCompanyResponse companyResponse = PartialCompanyResponse.builder()
                .id(company != null ? company.getId() : null)
                .name(company !=null ? company.getName(): null)
                .build();

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .address(user.getAddress())
                .photo(user.getPhoto())
                .phone(user.getPhone())
                .roles(user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
                .status(user.getStatus())
                .createdBy(user.getCreatedBy())
                .companyResponse(companyResponse)
                .build();
    }

    @Override
    public void changeUserStatus(Long id, String status) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        StatusCode.USER_NOT_FOUND.getMessage(),
                        StatusCode.USER_NOT_FOUND.getCode()
                )
        );
        user.setStatus(Status.valueOf(status));
        userRepository.save(user);
    }
}
