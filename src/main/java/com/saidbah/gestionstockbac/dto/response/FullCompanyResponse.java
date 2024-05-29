package com.saidbah.gestionstockbac.dto.response;

import com.saidbah.gestionstockbac.entity.Company;
import com.saidbah.gestionstockbac.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FullCompanyResponse {

    private Long id;

    private String name;

    private String description;

    private String email;

    private String address;

    private String city;

    private String country;

    private String photo;

    private String phone;

    private String createdBy;

    private String createdAt;

    private Status status;
}

