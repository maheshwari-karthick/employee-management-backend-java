package com.employee_management.model;

public enum Role {
    ADMIN,
    USER;

    public String getRole() {
        return switch (this) {
            case ADMIN -> "ADMIN";
            case USER -> "USER";
        };
    }
}
