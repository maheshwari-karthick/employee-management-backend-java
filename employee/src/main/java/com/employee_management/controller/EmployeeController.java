package com.employee_management.controller;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Employee;
import com.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

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
    public ResponseEntity<String> addEmployee(@RequestBody @Valid AddEmployeeRequest AddEmployeeRequest) {
        log.info("Add employee : " + AddEmployeeRequest.toString());
        long employeeId = employeeService.addEmployee(AddEmployeeRequest.toEmployee());
        log.info("Employee with id " + employeeId + " added");
        return new ResponseEntity<>("Employee with id " + employeeId + " added", HttpStatus.CREATED);
    }

    @PutMapping("/employee")
    public ResponseEntity<String> updateEmployee(@RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest) {
        log.info("Update employee : " + updateEmployeeRequest.toString());
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

    @GetMapping("/employee/{departmentId}")
    public ResponseEntity<List<Employee>> getEmployeeByDepartment(@PathVariable long departmentId) throws Exception {
        log.info("Get employee by department : " + departmentId);
        List<Employee> employees = employeeService.getDepartmentById(departmentId);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}


