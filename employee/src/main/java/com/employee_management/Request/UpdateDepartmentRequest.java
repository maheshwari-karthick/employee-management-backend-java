package com.employee_management.Request;

import com.employee_management.model.Department;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDepartmentRequest {
    @NonNull
    private Long id;

    @NotBlank(message = "departmentName should not be blank.")
    private String departmentName;

    private String departmentPhone;
    private String departmentEmail;
    private String organizationName;
    private String organizationAddress;
    private String departmentHead;

    public Department toDepartment() {
        return Department.builder()
                .id(id)
                .departmentName(departmentName)
                .departmentPhone(departmentPhone)
                .departmentEmail(departmentEmail)
                .organizationName(organizationName)
                .organizationAddress(organizationAddress)
                .departmentHead(departmentHead)
                .build();
    }
}
