package com.employee_management.Request;

import com.employee_management.model.Department;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDepartmentRequest {
    @NonNull
    private Long id;

    @NotBlank(message = "departmentName should not be blank.")
    private String departmentName;

    @Pattern(regexp = "^\\d{10}$",message = "departmentPhone is invalid.")
    private String departmentPhone;

    @Email(message = "departmentEmail is invaild.")
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
