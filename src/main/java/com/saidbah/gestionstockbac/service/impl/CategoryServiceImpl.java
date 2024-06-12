package com.saidbah.gestionstockbac.service.impl;

import com.saidbah.gestionstockbac.dto.request.CategoryRequest;
import com.saidbah.gestionstockbac.dto.response.CategoryResponse;
import com.saidbah.gestionstockbac.entity.Category;
import com.saidbah.gestionstockbac.entity.Company;
import com.saidbah.gestionstockbac.entity.User;
import com.saidbah.gestionstockbac.exception.EntityAlreadyExistsException;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import com.saidbah.gestionstockbac.repository.CategoryRepository;
import com.saidbah.gestionstockbac.repository.CompanyRepository;
import com.saidbah.gestionstockbac.repository.UserRepository;
import com.saidbah.gestionstockbac.service.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    @Override
    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByCode(request.getCode())) {
            throw new EntityAlreadyExistsException("Category name already exist", 400);
        }

        if (categoryRepository.existsByDesignation(request.getDesignation())) {
            throw new EntityAlreadyExistsException("Category designation already exist", 400);
        }

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();

        User user = userRepository.findByEmail(currentUserEmail).orElseThrow(
                () -> new EntityNotFoundException("User not found", 400)
        );

        Company company = companyRepository.findById(user.getCompany().getId()).orElseThrow(
                () -> new EntityNotFoundException("CompanyResponse not found", 400)
        );

        var category = Category.builder()
                .code(request.getCode())
                .designation(request.getDesignation())
                .user(user)
                .company(company)
                .build();
        categoryRepository.save(category);

        return CategoryResponse.builder()
                .id(category.getId())
                .code(category.getCode())
                .designation(category.getDesignation())
                .build();
    }
}
