package com.saidbah.gestionstockbac.dto.request;

import com.saidbah.gestionstockbac.entity.Role;
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
public class RegisterRequest {

    private String firstname;

    private String lastname;

    private String email;

    private String phone;

    private String address;

    private String password;

    private List<Role> roles;

    private String photo;

    private Status status;

    private String createdBy;

    private Long companyId;
}
