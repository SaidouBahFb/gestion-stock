package com.saidbah.gestionstockbac.dto.response;

import com.saidbah.gestionstockbac.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String firstname;

    private String lastname;

    private String email;

    private String phone;

    private String address;

    private List<String> roles;

    private String photo;

    private Status status;

    private String createdBy;

    private PartialCompanyResponse companyResponse;

    private FullCompanyResponse company;

}
