package com.employee_management.service;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.Request.UpdateDepartmentRequest;
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

    public boolean existsDepartmentByName(String name) {
        return departmentRepository.existsByDepartmentName(name);
    }

    public Optional<Department> getDepartmentByName(String name) {
        return departmentRepository.findByDepartmentName(name);
    }

    public boolean existsDepartmentById(Long id) {
        return !departmentRepository.existsById(id);
    }

    public String existsDepartmentByNameListBeforeAdding(List<String> names) {
        StringBuilder resultString = new StringBuilder();
        for (String name : names) {
            if (departmentRepository.existsByDepartmentName(name)) {
                resultString.append("Department Name ").append(name)
                        .append(" already present.")
                        .append(System.lineSeparator());
            }
        }
        return resultString.toString();
    }

    public String existsDepartmentByNameListBeforeUpdating(List<UpdateDepartmentRequest> updateDepartmentRequestList) {
        StringBuilder resultString = new StringBuilder();
        for (UpdateDepartmentRequest updateDepartmentRequest : updateDepartmentRequestList) {
            Optional<Department> department = departmentRepository.findByDepartmentName(updateDepartmentRequest.getDepartmentName());
            if (department.isPresent() && department.get().getId() != updateDepartmentRequest.getId()) {
                resultString.append("Department Name ").append(updateDepartmentRequest.getDepartmentName())
                        .append(" already present.").append(System.lineSeparator());
            }
        }
        return resultString.toString();
    }

    public void addDepartmentList(List<AddDepartmentRequest> addDepartmentRequestList) {
        for (AddDepartmentRequest addDepartmentRequest : addDepartmentRequestList) {
            departmentRepository.save(addDepartmentRequest.toDepartment());
        }
    }

    public void updateDepartmentList(List<UpdateDepartmentRequest> updateDepartmentRequestList) {
        for (UpdateDepartmentRequest updateDepartmentRequest : updateDepartmentRequestList) {
            departmentRepository.save(updateDepartmentRequest.toDepartment());
        }
    }

    public void deleteAllDepartmentsByIdInBatch(List<Long> departmentIds) throws Exception {
        departmentRepository.deleteAllByIdInBatch(departmentIds);
    }

    public void deleteAllDepartments(){
        departmentRepository.deleteAll();
    }
}