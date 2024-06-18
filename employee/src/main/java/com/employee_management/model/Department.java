package com.employee_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DEPARTMENT")
@Getter
@Setter
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


    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", departmentName='" + departmentName + '\'' +
                ", departmentPhone='" + departmentPhone + '\'' +
                ", departmentEmail='" + departmentEmail + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationAddress='" + organizationAddress + '\'' +
                ", departmentHead='" + departmentHead + '\'' +
                '}';
    }
}