package com.saidbah.gestionstockbac.utils;

import com.saidbah.gestionstockbac.apiResponse.ApiResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityBuilder {
    public static ResponseEntity<ApiResponse<Object>> buildResponseEntityObjet(Object responseData, int statusCode, String message) {
        ApiResponse<Object> response = ApiResponse.<Object>builder()
                .data(responseData)
                .statusCode(statusCode)
                .message(message)
                .build();
        return ResponseEntity.ok(response);
    }

    public static <T> ResponseEntity<ApiResponse<List<T>>> buildResponseEntityList (List<T> responseData, int statusCode, String message) {
        ApiResponse<List<T>> response = ApiResponse.<List<T>>builder()
                .data(responseData)
                .statusCode(statusCode)
                .message(message)
                .build();

        return ResponseEntity.ok(response);
    }
}
