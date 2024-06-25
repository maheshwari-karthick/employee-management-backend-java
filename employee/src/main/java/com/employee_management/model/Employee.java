package com.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "EMPLOYEE")
@Data
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

}