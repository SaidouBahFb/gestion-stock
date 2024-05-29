package com.saidbah.gestionstockbac.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EntityAlreadyExistsException extends RuntimeException {
    private final String message;
    private final int code;
}
