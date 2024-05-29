package com.saidbah.gestionstockbac.dto.request;

import com.saidbah.gestionstockbac.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyRequest {

    private String name;

    private String description;

    private String email;

    private String address;

    private String city;

    private String country;

    private String photo;

    private String phone;

    private Status status;

    private String createdBy;
}
