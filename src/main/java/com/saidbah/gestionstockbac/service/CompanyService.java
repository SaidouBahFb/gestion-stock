package com.saidbah.gestionstockbac.service;

import com.saidbah.gestionstockbac.dto.request.CompanyRequest;
import com.saidbah.gestionstockbac.dto.response.FullCompanyResponse;

import java.util.List;

public interface CompanyService {
    FullCompanyResponse create(CompanyRequest request);
    List<FullCompanyResponse> getAllCompanies();
    FullCompanyResponse updateCompany(Long id,CompanyRequest request);
    void changeCompanyStatus(Long id, String status);
}
