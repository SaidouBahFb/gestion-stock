package com.saidbah.gestionstockbac.controller;

import com.saidbah.gestionstockbac.apiResponse.ApiResponse;
import com.saidbah.gestionstockbac.dto.request.CompanyRequest;
import com.saidbah.gestionstockbac.dto.response.CompanyResponse;
import com.saidbah.gestionstockbac.exception.EntityNotFoundException;
import com.saidbah.gestionstockbac.repository.CompanyRepository;
import com.saidbah.gestionstockbac.service.CompanyService;
import com.saidbah.gestionstockbac.utils.ResponseEntityBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.text.SimpleDateFormat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companyResponse")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final Path location = Paths.get("D:\\images\\companies");

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody CompanyRequest request) {
        var response = companyService.create(request);
        return ResponseEntityBuilder.buildResponseEntityObjet(response, 200, "CompanyResponse added successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CompanyResponse>>> getAllUsers() {
        List<CompanyResponse> response = companyService.getAllCompanies();
        return ResponseEntityBuilder.buildResponseEntityList(response, 200, "List of all companies retrieved successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable Long id, @RequestBody CompanyRequest request) {
        CompanyResponse response = companyService.updateCompany(id, request);
        return ResponseEntityBuilder.buildResponseEntityObjet(response, 200, "CompanyResponse updated successfully");
    }

    @PostMapping("/download-img/{companyId}")
    public ResponseEntity<ApiResponse<Object>> downloadImg(
            @RequestParam("photo") MultipartFile img,
            @PathVariable Long companyId
    ) throws IOException {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = dateFormat.format(currentDate);

        String randomFileName = companyId.toString() + "_" + UUID.randomUUID().toString()  + "_" + formattedDate + "_" + img.getOriginalFilename();

        Path fileLocation = this.location.resolve(randomFileName);

        Files.copy(img.getInputStream(), fileLocation, StandardCopyOption.REPLACE_EXISTING);

        com.saidbah.gestionstockbac.entity.Company company = companyRepository.findById(companyId).orElseThrow(() -> new EntityNotFoundException("CompanyResponse not found", 400));
        company.setPhoto(fileLocation.toString());
        companyRepository.save(company);

        return ResponseEntity.ok(ApiResponse.<Object>builder()
                .data(fileLocation.toString())
                .statusCode(200)
                .message("Photo uploaded successfully")
                .build());
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<ApiResponse<Object>> changeStatus(@PathVariable Long id, @RequestParam String status){
        companyService.changeCompanyStatus(id, status);
        return ResponseEntityBuilder.buildResponseEntityObjet(null, 200, "CompanyResponse change successfully");
    }

}
