package com.saidbah.gestionstockbac.service.impl;

import com.saidbah.gestionstockbac.dto.request.CompanyRequest;
import com.saidbah.gestionstockbac.dto.response.CompanyResponse;
import com.saidbah.gestionstockbac.entity.Status;
import com.saidbah.gestionstockbac.exception.EntityAlreadyExistsException;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import com.saidbah.gestionstockbac.repository.CompanyRepository;
import com.saidbah.gestionstockbac.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    @Override
    public CompanyResponse create(CompanyRequest request) {

        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new EntityAlreadyExistsException("Email already in use", 400);
        }

        if (companyRepository.existsByName(request.getName())) {
            throw new EntityAlreadyExistsException("CompanyResponse name already in use", 400);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();

        var company = com.saidbah.gestionstockbac.entity.Company.builder()
                .name(request.getName())
                .description(request.getDescription())
                .email(request.getEmail())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .phone(request.getPhone())
                .status(Status.ACTIVE)
                .createdBy(currentUserEmail)
                .build();
        companyRepository.save(company);

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .email(company.getEmail())
                .address(company.getAddress())
                .city(company.getCity())
                .country(company.getCountry())
                .phone(company.getPhone())
                .status(company.getStatus())
                .createdBy(company.getCreatedBy())
                .build();
    }

    @Override
    public List<CompanyResponse> getAllCompanies() {
        List<com.saidbah.gestionstockbac.entity.Company> companies = companyRepository.findAll();
        List<CompanyResponse> response = companies.stream()
                .map(company -> CompanyResponse.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .description(company.getDescription())
                        .address(company.getAddress())
                        .email(company.getEmail())
                        .phone(company.getPhone())
                        .city(company.getCity())
                        .country(company.getCountry())
                        .photo(company.getPhoto())
                        .status(company.getStatus())
                        .createdBy(company.getCreatedBy())
                        .createdAt(String.valueOf(company.getCreatedAt()))
                        .build()
                ).collect(Collectors.toList());
        return response;
    }

    @Override
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("CompanyResponse with ID " + id +" not found", 404);
        }

        var company = companyRepository.findByEmail(request.getEmail()).orElse(null);
        if (company != null && !company.getId().equals(id)) {
            throw new EntityAlreadyExistsException("Email already in use", 400);
        }

        company.setName(request.getName());
        company.setDescription(request.getDescription());
        company.setEmail(request.getEmail());
        company.setCity(request.getCity());
        company.setCountry(request.getCountry());
        company.setPhone(request.getPhone());
        company.setAddress(request.getAddress());

        companyRepository.save(company);

        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .email(company.getEmail())
                .address(company.getAddress())
                .phone(company.getPhone())
                .city(company.getCity())
                .country(company.getCountry())
                .photo(company.getPhoto())
                .status(company.getStatus())
                .build();
    }

    @Override
    public void changeCompanyStatus(Long id, String status) {
        com.saidbah.gestionstockbac.entity.Company company = companyRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("CompanyResponse with ID " + id + " not found",  404)
        );
        company.setStatus(Status.valueOf(status));
        companyRepository.save(company);
    }
}
