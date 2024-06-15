package com.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String firstName;

    private String lastName;
    private String email;
    private String phone;
    private Gender gender;
    private int age;
    private double salary;
    private Role role;
    private String address;

    @Column(nullable = false)
    private long departmentId;

    @Override
    public String toString() {
        return "Employee{" +
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