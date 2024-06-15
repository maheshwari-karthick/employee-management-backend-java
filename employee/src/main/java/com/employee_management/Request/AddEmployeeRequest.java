package com.employee_management.Request;

import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddEmployeeRequest {

    @NotBlank
    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private int age;
    private double salary;
    private String role;
    private String address;

    @NotEmpty
    private long departmentId;

    public Employee toEmployee() {
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .gender(Gender.valueOf(gender))
                .age(age)
                .salary(salary)
                .role(Role.valueOf(role))
                .address(address)
                .departmentId(departmentId)
                .build();
    }

    @Override
    public String toString() {
        return "AddEmployeeRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", age='" + age + '\'' +
                ", salary='" + salary + '\'' +
                ", role='" + role + '\'' +
                ", address='" + address + '\'' +
                ", departmentId='" + departmentId + '\'' +
                '}';
    }


}
