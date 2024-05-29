package com.saidbah.gestionstockbac.service;

import com.saidbah.gestionstockbac.dto.request.CategoryRequest;
import com.saidbah.gestionstockbac.dto.response.CategoryResponse;

public interface CategoryService {
    CategoryResponse create(CategoryRequest request);
}
