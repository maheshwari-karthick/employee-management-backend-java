package com.employee_management.response;

import com.employee_management.model.Role;
import com.employee_management.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetUserResponse {
    private long id;
    private String username;
    private List<Role> roles;

    public GetUserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.roles = user.getRoles();
    }
}
