package com.employee_management.Request;

import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddEmployeeRequest {

    @NotBlank(message = "firstName should  not be blank.")
    private String firstName;

    private String lastName;

    @Email(message = "email is invalid.")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "phone is invalid.")
    private String phone;

    private Gender gender;

    @Positive(message = "age should be positive.")
    private Integer age;

    @Positive(message = "salary should be positive.")
    private Double salary;
    private Role role;
    private String address;

    @NonNull
    private Long departmentId;

    public Employee toEmployee() {
        return Employee.builder()
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
