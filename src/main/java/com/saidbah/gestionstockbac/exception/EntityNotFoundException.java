package com.saidbah.gestionstockbac.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ResponseStatus(code = HttpStatus.FORBIDDEN)
public class EntityNotFoundException extends RuntimeException {
    private final String message;
    private final int code;
}
