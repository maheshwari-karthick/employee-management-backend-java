package com.employee_management.Request;

import com.employee_management.model.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDepartmentRequest {

    @NotBlank
    private String departmentName;

    private String departmentPhone;
    private String departmentEmail;
    private String organizationName;
    private String organizationAddress;
    private String departmentHead;

    public Department toDepartment() {
        return Department.builder()
                .departmentName(departmentName)
                .departmentPhone(departmentPhone)
                .departmentEmail(departmentEmail)
                .organizationName(organizationName)
                .organizationAddress(organizationAddress)
                .departmentHead(departmentHead)
                .build();
    }

    @Override
    public String toString() {
        return "AddDepartmentRequest{" +
                "departmentName='" + departmentName + '\'' +
                ", departmentPhone='" + departmentPhone + '\'' +
                ", departmentEmail='" + departmentEmail + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationAddress='" + organizationAddress + '\'' +
                ", departmentHead='" + departmentHead + '\'' +
                '}';
    }
}
