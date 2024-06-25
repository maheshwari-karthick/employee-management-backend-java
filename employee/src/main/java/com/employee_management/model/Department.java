package com.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "DEPARTMENT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String departmentName;

    private String departmentPhone;
    private String departmentEmail;
    private String organizationName;
    private String organizationAddress;
    private String departmentHead;
}