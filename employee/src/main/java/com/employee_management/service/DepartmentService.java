package com.employee_management.service;

import com.employee_management.model.Department;
import com.employee_management.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department getDepartmentById(Long id) throws Exception {
        Optional<Department> department = departmentRepository.findById(id);
        if (department.isPresent()) {
            return department.get();
        }
        throw new NotFoundException();
    }

    public long addDepartment(Department department) {
        return departmentRepository.save(department).getId();
    }

    public long updateDepartment(Department department) {
        return departmentRepository.save(department).getId();
    }

    public void deleteDepartmentById(Long id) {
        departmentRepository.deleteById(id);
    }

    public List<Department> getDepartmentsByDepartmentName(String departmentName) {
        return null;
    }

    public List<Department> getDepartmentsByOrganizationName(String organizationName) {
        return null;
    }
}
