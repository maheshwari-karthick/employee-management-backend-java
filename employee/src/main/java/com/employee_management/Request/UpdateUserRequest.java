package com.employee_management.Request;

import com.employee_management.model.Role;
import com.employee_management.model.User;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NonNull
    private Long id;
    @NotBlank(message = "userName should not be blank.")
    private String userName;
    @NotBlank(message = "password should not be blank.")
    private String password;

    private List<Role> roles;

    public User toUser() {
        return User.builder()
                .id(id)
                .username(userName)
                .password(password)
                .roles(roles)
                .build();
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }
}
