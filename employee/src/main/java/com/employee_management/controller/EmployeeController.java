package com.employee_management.controller;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Employee;
import com.employee_management.service.DepartmentService;
import com.employee_management.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    @GetMapping("/employee/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Get All Employees");
        List<Employee> employees = employeeService.getAllEmployees();
        log.info("Employee List: " + employees.toString());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable long employeeId) throws Exception {
        log.info("Get employee by id : " + employeeId);
        Employee employee = employeeService.getEmployeeById(employeeId);
        log.info("Employee : " + employee.toString());
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping("/employee")
    public ResponseEntity<String> addEmployee(@RequestBody @Valid AddEmployeeRequest addEmployeeRequest,
                                              BindingResult bindingResult) throws Exception {
        log.info("Add employee : " + addEmployeeRequest.toString());

        if (bindingResult.hasErrors()) {
            log.error("Binding Result in addEmployee" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        if (departmentService.existsDepartmentById(addEmployeeRequest.getDepartmentId())) {
            log.info("Department does not exists");
            return new ResponseEntity<>("Department does not exists", HttpStatus.CONFLICT);
        }

        long employeeId = employeeService.addEmployee(addEmployeeRequest.toEmployee());
        log.info("Employee with id " + employeeId + " added");
        return new ResponseEntity<>("Employee with id " + employeeId + " added", HttpStatus.CREATED);
    }

    @PutMapping("/employee")
    public ResponseEntity<String> updateEmployee(@RequestBody @Valid UpdateEmployeeRequest updateEmployeeRequest,
                                                 BindingResult bindingResult) throws Exception {
        log.info("Update employee : " + updateEmployeeRequest.toString());

        if (bindingResult.hasErrors()) {
            log.error("Binding Result in updateEmployee" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        if (departmentService.existsDepartmentById(updateEmployeeRequest.getDepartmentId())) {
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

    @DeleteMapping("/employees")
    public ResponseEntity<String> deleteEmployees(@RequestBody List<Long> employeeIds) throws Exception {
        log.info("Delete Employees : " + employeeIds.toString());
        if (employeeIds.isEmpty()) {
            log.info("Employee Ids are empty");
            return new ResponseEntity<>("Employee Ids are empty", HttpStatus.CONFLICT);
        }
        employeeService.deleteAllByIdInBatch(employeeIds);
        log.info("Employees deleted");
        return new ResponseEntity<>("Employees deleted", HttpStatus.OK);
    }

    @PostMapping("/employees")
    public ResponseEntity<String> addEmployeeList(@RequestBody @Valid List<AddEmployeeRequest> addEmployeeRequestList,
                                                  BindingResult bindingResult) throws Exception {
        log.info("Add Employees : " + addEmployeeRequestList.toString());
        if (bindingResult.hasErrors()) {
            log.error("Binding Results in addEmployeeList" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        String validationResult = employeeService.validateEmployeesBeforeAdding(addEmployeeRequestList);
        if (validationResult.isEmpty()) {
            employeeService.addEmployeeList(addEmployeeRequestList);
            return new ResponseEntity<>("Employees Added", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(validationResult, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/employees")
    public ResponseEntity<String> updateEmployeeList(@RequestBody @Valid List<UpdateEmployeeRequest> updateEmployeeRequestList, BindingResult bindingResult) {
        log.info("Update Employees : " + updateEmployeeRequestList.toString());
        if (bindingResult.hasErrors()) {
            log.error("Binding Results in updateEmployeeList" + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            return new ResponseEntity<>(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        String validationResult = employeeService.validateEmployeesBeforeUpdating(updateEmployeeRequestList);
        if (validationResult.isEmpty()) {
            employeeService.updateEmployeeList(updateEmployeeRequestList);
            return new ResponseEntity<>("Employees Updated", HttpStatus.OK);
        }
        return new ResponseEntity<>(validationResult, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("employee/all")
    public ResponseEntity<String> deleteEmployeeList() throws Exception {
        log.info("Delete Employees all");
        employeeService.deleteAllEmployees();
        return new ResponseEntity<>(" All the Employees are Deleted", HttpStatus.OK);
    }
}

