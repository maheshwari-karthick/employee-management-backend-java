package com.employee_management.Request;

import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeRequest {
    @NonNull
    private Long id;

    @NotBlank(message = "firstName should not be blank.")
    private String firstName;

    private String lastName;

    @Email(message = "email is invalid.")
    private String email;

    @Pattern(regexp = "^\\d{10}$",message = "phone is invalid.")
    private String phone;

    private Gender gender;
    private Integer age;
    private Double salary;
    private Role role;
    private String address;

    @NonNull
    private Long departmentId;

    public Employee toEmployee() {
        return Employee.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .gender(gender)
                .age(age)
                .salary(salary)
                .role(role)
                .address(address)
                .departmentId(departmentId)
                .build();
    }
}
