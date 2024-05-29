package com.saidbah.gestionstockbac.exception;

public enum StatusCode {
    USER_RETRIEVED_SUCCESSFULLY(1000, "List of all users retrieved successfully."),
    USER_ADDED_SUCCESSFULLY(1001, "User added successfully."),
    USER_UPDATED_SUCCESSFULLY(1002, "User updated successfully."),
    USER_CHANGE_STATUS_SUCCESSFULLY(1003, "User status changed successfully."),
    USER_NOT_FOUND(1004, "User not found"),
    USER_ALREADY_EXISTS(1005, "User email already exists"),
    USER_INACTIVE(1006, "User inactive"),
    USER_EMAIL_OR_PASSWORD_INVALID(1007, "Invalid email or password"),
    USER_REGISTER_SUCCESSFULLY(1008, "User registration completed successfully"),
    USER_AUTHENTICATED_SUCCESSFULLY(1009, "User authenticated successfully"),
    COMPANY_RETRIEVED_SUCCESSFULLY(2000, "List of all companies retrieved successfully."),
    COMPANY_ADDED_SUCCESSFULLY(2001, "Company added successfully."),
    COMPANY_UPDATED_SUCCESSFULLY(2002, "Company updated successfully."),
    COMPANY_CHANGE_STATUS_SUCCESSFULLY(1003, "User status changed successfully."),
    COMPANY_NOT_FOUND(2004, "Company not found"),
    COMPANY_ALREADY_EXISTS(2005, "Company email already exists"),
    COMPANY_INACTIVE(2006, "Company inactive");

    private final int code;
    private final String message;

    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
