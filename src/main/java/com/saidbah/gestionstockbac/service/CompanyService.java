package com.saidbah.gestionstockbac.service;

import com.saidbah.gestionstockbac.dto.request.CompanyRequest;
import com.saidbah.gestionstockbac.dto.response.CompanyResponse;

import java.util.List;

public interface CompanyService {
    CompanyResponse create(CompanyRequest request);
    List<CompanyResponse> getAllCompanies();
    CompanyResponse updateCompany(Long id, CompanyRequest request);
    void changeCompanyStatus(Long id, String status);
}
