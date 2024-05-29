package com.saidbah.gestionstockbac.controller;

import com.saidbah.gestionstockbac.apiResponse.ApiResponse;
import com.saidbah.gestionstockbac.dto.request.UserRequest;
import com.saidbah.gestionstockbac.dto.response.UserResponse;
import com.saidbah.gestionstockbac.exception.StatusCode;
import com.saidbah.gestionstockbac.service.UserService;
import com.saidbah.gestionstockbac.utils.ResponseEntityBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntityBuilder.buildResponseEntityList(
                users,
                StatusCode.USER_RETRIEVED_SUCCESSFULLY.getCode(),
                StatusCode.USER_RETRIEVED_SUCCESSFULLY.getMessage()
        );
    }

    @GetMapping("/users-by-company/{id}")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getUsersByCompany(@PathVariable Long id) {
        List<UserResponse> users = userService.getUserByCompany(id);
        return ResponseEntityBuilder.buildResponseEntityList(
                users, StatusCode.USER_RETRIEVED_SUCCESSFULLY.getCode(),
                StatusCode.USER_RETRIEVED_SUCCESSFULLY.getMessage()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody UserRequest request){
        var response = userService.create(request);
        return ResponseEntityBuilder.buildResponseEntityObjet(
                response,
                StatusCode.USER_ADDED_SUCCESSFULLY.getCode(),
                StatusCode.USER_ADDED_SUCCESSFULLY.getMessage()
        );
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Object>> update(@PathVariable("userId") Long id, @RequestBody UserRequest request){
        var response = userService.updateUser(id,request);
        return ResponseEntityBuilder.buildResponseEntityObjet(
                response,
                StatusCode.USER_UPDATED_SUCCESSFULLY.getCode(),
                StatusCode.USER_UPDATED_SUCCESSFULLY.getMessage()
        );
    }

    @PutMapping("/change-status/{id}")
    public ResponseEntity<ApiResponse<Object>> changeStatus(@PathVariable Long id, @RequestParam String status){
        userService.changeUserStatus(id,status);
        return ResponseEntityBuilder.buildResponseEntityObjet(
                null,
                StatusCode.USER_CHANGE_STATUS_SUCCESSFULLY.getCode(),
                StatusCode.USER_CHANGE_STATUS_SUCCESSFULLY.getMessage()
        );
    }

}
