package com.employee_management.Request;

import com.employee_management.model.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddDepartmentRequest {

    @NotBlank(message = "departmentName should not be blank.")
    private String departmentName;

    @Pattern(regexp = "^\\d{10}$",message = "departmentPhone is invalid.")
    private String departmentPhone;

    @Email(message = "departmentEmail is invalid.")
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
}
