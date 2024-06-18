package com.employee_management.controller;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.Request.UpdateDepartmentRequest;
import com.employee_management.model.Department;
import com.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class DepartmentController {

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
    public ResponseEntity<String> addDepartment(@RequestBody @Valid AddDepartmentRequest addDepartmentRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        if (departmentService.existsDepartmentByName(addDepartmentRequest.getDepartmentName())) {
            return new ResponseEntity<>("Department Already Exists", HttpStatus.CONFLICT);
        }
        log.info("Add Department : " + addDepartmentRequest.toString());
        long departmentId = departmentService.addDepartment(addDepartmentRequest.toDepartment());
        return new ResponseEntity<>("Department added successfully.DepartmentId : " + departmentId, HttpStatus.CREATED);
    }

    @PutMapping("/department")
    public ResponseEntity<String> updateDepartment(@RequestBody @Valid UpdateDepartmentRequest updateDepartmentRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        if (departmentService.existsDepartmentByName(updateDepartmentRequest.getDepartmentName())) {
            Optional<Department> optionalDepartment = departmentService.getDepartmentByName(updateDepartmentRequest.getDepartmentName());
            if (optionalDepartment.isPresent() && optionalDepartment.get().getId() != updateDepartmentRequest.getId()) {
                return new ResponseEntity<>("Department Already Exists", HttpStatus.CONFLICT);
            }
        }
        log.info("Update Department : " + updateDepartmentRequest.toString());
        long updatedDepartmentId = departmentService.updateDepartment(updateDepartmentRequest.toDepartment());
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
