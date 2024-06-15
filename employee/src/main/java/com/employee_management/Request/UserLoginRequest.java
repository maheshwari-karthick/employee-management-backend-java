package com.employee_management.Request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequest {
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
}
