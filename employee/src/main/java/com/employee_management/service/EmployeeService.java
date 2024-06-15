package com.employee_management.service;

import com.employee_management.model.Employee;
import com.employee_management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

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

    //    public List<Employee> getEmployeeByName(String name) {
//        return employeeRepository.findByName(name);
//    }
    public List<Employee> getEmployeeByGender(String gender) {
        return null;
    }

    public List<Employee> getEmployeeByAge(String age) {
        return null;
    }

    public List<Employee> getEmployeeByRole(String role) {
        return null;
    }

    public List<Employee> getEmployeeBySalary(double salary) {
        return null;
    }

    public List<Employee> getEmployeeBySalaryBetween(double salary1, double salary2) {
        return null;
    }

    public List<Employee> getEmployeeBySalaryGreaterThan(double salary) {
        return null;
    }

}
