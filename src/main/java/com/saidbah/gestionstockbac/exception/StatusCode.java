package com.saidbah.gestionstockbac.exception;

public enum StatusCode {
    USER_RETRIEVED_SUCCESSFULLY(1000, "Liste de tous les utilisateurs récupérés avec succès."),
    USER_ADDED_SUCCESSFULLY(1001, "L'utilsateur a été ajouté avec succès."),
    USER_UPDATED_SUCCESSFULLY(1002, "L'utilisateur a été modifié avec succès."),
    USER_CHANGE_STATUS_SUCCESSFULLY(1003, "Le statut de l'utilisateur a été changé avec succès."),
    USER_NOT_FOUND(1004, "Cet utilisateur n'existe pas"),
    USER_ALREADY_EXISTS(1005, "Cet utilisateur existe déjà"),
    USER_INACTIVE(1006, "Cet utilisateur a été bloqué"),
    USER_EMAIL_OR_PASSWORD_INVALID(1007, "Email ou mot de passe incorrect"),
    USER_REGISTER_SUCCESSFULLY(1008, "L'utilisateur a été enregistré avec succès"),
    USER_AUTHENTICATED_SUCCESSFULLY(1009, "L'utilisateur s'est connecté(e) avec succès"),
    COMPANY_RETRIEVED_SUCCESSFULLY(2000, "List of all companies retrieved successfully."),
    COMPANY_ADDED_SUCCESSFULLY(2001, "CompanyResponse added successfully."),
    COMPANY_UPDATED_SUCCESSFULLY(2002, "CompanyResponse updated successfully."),
    COMPANY_CHANGE_STATUS_SUCCESSFULLY(1003, "User status changed successfully."),
    COMPANY_NOT_FOUND(2004, "CompanyResponse not found"),
    COMPANY_ALREADY_EXISTS(2005, "CompanyResponse email already exists"),
    COMPANY_INACTIVE(2006, "CompanyResponse inactive");

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
