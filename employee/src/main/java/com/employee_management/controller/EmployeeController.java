package com.employee_management.controller;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Employee;
import com.employee_management.service.DepartmentService;
import com.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/employee/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Get All Employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long employeeId) throws Exception {
        log.info("Get employee by id : " + employeeId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/employee")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid AddEmployeeRequest addEmployeeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("Add employee : " + addEmployeeRequest.toString());
        if (!departmentService.existsDepartmentById(addEmployeeRequest.getDepartmentId())) {
            log.info("Department not found");
            return new ResponseEntity<>("Department does not exists", HttpStatus.CONFLICT);
        }
        long employeeId = employeeService.addEmployee(addEmployeeRequest.toEmployee());
        log.info("Employee with id " + employeeId + " added");
        return new ResponseEntity<>("Employee with id " + employeeId + " added", HttpStatus.CREATED);
    }

    @PutMapping("/employee")
    public ResponseEntity<String> updateEmployee(@RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("Update employee : " + updateEmployeeRequest.toString());
        if (!departmentService.existsDepartmentById(updateEmployeeRequest.getDepartmentId())) {
            log.info("Department not found");
            return new ResponseEntity<>("Department does not exists", HttpStatus.CONFLICT);
        }
        long updatedEmployeeId = employeeService.updateEmployee(updateEmployeeRequest.toEmployee());
        log.info("Employee with id " + updatedEmployeeId + " updated");
        return new ResponseEntity<>("Employee with id " + updatedEmployeeId + " updated", HttpStatus.OK);
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable long employeeId) throws Exception {
        log.info("Delete employee : " + employeeId);
        employeeService.getEmployeeById(employeeId);
        employeeService.deleteEmployee(employeeId);
        log.info("Employee with id " + employeeId + " deleted");
        return new ResponseEntity<>("Employee with id " + employeeId + " deleted", HttpStatus.OK);
    }
}


