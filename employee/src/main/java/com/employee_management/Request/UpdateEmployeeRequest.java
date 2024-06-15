package com.employee_management.Request;

import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateEmployeeRequest {

    @NotEmpty
    private long id;
    @NotEmpty
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private int age;
    private double salary;
    private Role role;
    private String address;
    @NotEmpty
    private long departmentId;

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

    @Override
    public String toString() {
        return "UpdateEmployeeRequest{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", salary=" + salary +
                ", role=" + role +
                ", address='" + address + '\'' +
                ", departmentId=" + departmentId +
                '}';
    }
}
