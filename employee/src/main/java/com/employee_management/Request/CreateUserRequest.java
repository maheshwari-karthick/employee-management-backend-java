package com.employee_management.Request;

import com.employee_management.model.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class CreateUserRequest {

    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    private List<String> roles;

    public User toUser() {
        return User.builder()
                .username(userName)
                .password(password)
                .roles(roles)
                .build();
    }
}
