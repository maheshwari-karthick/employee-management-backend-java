package com.employee_management.controller;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.model.Department;
import com.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DepartmentController {

    private static final Logger log = LoggerFactory.getLogger(DepartmentController.class);
    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/department/all")
    public ResponseEntity<List<Department>> getAllDepartments() {
        log.info("Get all department list");
        List<Department> departments = departmentService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable long departmentId) throws Exception {
        log.info("Get Department By Id : " + departmentId);
        Department department = departmentService.getDepartmentById(departmentId);
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PostMapping("/department")
    public ResponseEntity<String> addDepartment(@RequestBody @Valid AddDepartmentRequest AddDepartmentRequest) {
        log.info("Add Department : " + AddDepartmentRequest.toString());
        long departmentId = departmentService.addDepartment(AddDepartmentRequest.toDepartment());
        return new ResponseEntity<>("Department added successfully.DepartmentId : " + departmentId, HttpStatus.CREATED);
    }

    @PutMapping("/department")
    public ResponseEntity<String> updateDepartment(@RequestBody @Valid Department department){
        log.info("Update Department : " + department.toString());
        long updatedDepartmentId = departmentService.updateDepartment(department);
        return new ResponseEntity<>("Department updated successfully.DepartmentId : " + updatedDepartmentId, HttpStatus.OK);
    }

    @DeleteMapping("/department/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long departmentId) throws Exception {
        log.info("Delete Department : " + departmentId);
        departmentService.getDepartmentById(departmentId);
        departmentService.deleteDepartmentById(departmentId);
        return new ResponseEntity<>("Department deleted successfully.DepartmentId : " + departmentId, HttpStatus.OK);
    }


}
