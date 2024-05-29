package com.saidbah.gestionstockbac.service;

import com.saidbah.gestionstockbac.dto.request.UserRequest;
import com.saidbah.gestionstockbac.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse create(UserRequest request);
    List<UserResponse> getUserByCompany(Long companyId);
    UserResponse updateUser(Long id, UserRequest request);
    void changeUserStatus(Long id, String status);
}
