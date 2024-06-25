package com.employee_management.service;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Department;
import com.employee_management.model.Employee;
import com.employee_management.repository.DepartmentRepository;
import com.employee_management.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(long id) throws Exception {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return employee.get();
        }
        throw new NotFoundException();
    }

    public long addEmployee(Employee Employee) {

        return employeeRepository.save(Employee).getId();
    }

    public long updateEmployee(Employee employee) {

        return employeeRepository.save(employee).getId();
    }

    public void deleteEmployee(long id) {

        employeeRepository.deleteById(id);
    }

    public List<Employee> getDepartmentById(long id) throws Exception {
        Optional<List<Employee>> employees = employeeRepository.findByDepartmentId(id);
        if (employees.isPresent()) {
            return employees.get();
        }
        throw new NotFoundException();
    }

    public void deleteAllByIdInBatch(List<Long> ids) throws Exception {
        employeeRepository.deleteAllByIdInBatch(ids);
    }

    public void addEmployeeList(List<AddEmployeeRequest> addEmployeeRequestList) {
        List<Employee> employees = new ArrayList<>();
        for (AddEmployeeRequest addEmployeeRequest : addEmployeeRequestList) {
            employees.add(addEmployeeRequest.toEmployee());
        }
        employeeRepository.saveAll(employees);
    }

    public String validateEmployeesBeforeAdding(List<AddEmployeeRequest> addEmployeeRequestList) {
        StringBuilder result = new StringBuilder();
        for (AddEmployeeRequest addEmployeeRequest : addEmployeeRequestList) {
            Optional<Department> department = departmentRepository.findById(addEmployeeRequest.getDepartmentId());
            if (department.isEmpty()) {
                result.append("For Employee ").append(addEmployeeRequest.getFirstName())
                        .append(" department ").append(addEmployeeRequest.getDepartmentId())
                        .append(" is not present.")
                        .append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    public String validateEmployeesBeforeUpdating(List<UpdateEmployeeRequest> updateEmployeeRequestList) {
        StringBuilder result = new StringBuilder();
        for (UpdateEmployeeRequest updateEmployeeRequest : updateEmployeeRequestList) {
            Optional<Department> department = departmentRepository.findById(updateEmployeeRequest.getDepartmentId());
            if (department.isEmpty()) {
                result.append("For Employee ").append(updateEmployeeRequest.getFirstName())
                        .append(" department ").append(updateEmployeeRequest.getDepartmentId())
                        .append(" is not present").append(System.lineSeparator());
            }
        }
        return result.toString();
    }

    public void updateEmployeeList(List<UpdateEmployeeRequest> updateEmployeeRequestList) {
        List<Employee> employees = new ArrayList<>();
        for (UpdateEmployeeRequest updateEmployeeRequest : updateEmployeeRequestList) {
            employees.add(updateEmployeeRequest.toEmployee());
        }
        employeeRepository.saveAll(employees);
    }

    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }
}

