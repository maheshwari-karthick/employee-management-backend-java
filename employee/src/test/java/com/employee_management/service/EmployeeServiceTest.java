package com.employee_management.service;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Department;
import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import com.employee_management.repository.DepartmentRepository;
import com.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeServiceTest {
    @MockBean
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @Test
    public void shouldGetAllEmployees() {
        ArrayList<Employee> employeeList = new ArrayList<>();
        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<Employee> actualAllEmployees = employeeService.getAllEmployees();

        verify(employeeRepository).findAll();
        assertTrue(actualAllEmployees.isEmpty());
        assertSame(employeeList, actualAllEmployees);
    }

    @Test
    public void shouldGetEmployeeById() throws Exception {
        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        Optional<Employee> ofResult = Optional.of(employee);
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Employee actualEmployeeById = employeeService.getEmployeeById(1L);

        verify(employeeRepository).findById(eq(1L));
        assertSame(employee, actualEmployeeById);
    }

    @Test
    public void shouldGetEmployeeByIdThrowsException() throws Exception {

        Optional<Employee> emptyResult = Optional.empty();
        when(employeeRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> employeeService.getEmployeeById(1L));
        verify(employeeRepository).findById(eq(1L));
    }

    @Test
    public void shouldAddEmployee() {

        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);
        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee);

        long actualAddEmployeeResult = employeeService.addEmployee(employee);
        verify(employeeRepository).save(isA(Employee.class));
        assertEquals(1L, actualAddEmployeeResult);
    }

    @Test
    public void shouldUpdateEmployee() {
        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        when(employeeRepository.save(Mockito.<Employee>any())).thenReturn(employee);

        long actualUpdateEmployeeResult = employeeService.updateEmployee(employee);
        verify(employeeRepository).save(isA(Employee.class));
        assertEquals(1L, actualUpdateEmployeeResult);
    }


    @Test
    public void shouldDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(Mockito.<Long>any());
        employeeService.deleteEmployee(1L);
        verify(employeeRepository).deleteById(eq(1L));
        assertTrue(employeeService.getAllEmployees().isEmpty());
    }

    @Test
    void shouldGetDepartmentById() throws Exception {

        ArrayList<Employee> employeeList = new ArrayList<>();
        Optional<List<Employee>> ofResult = Optional.of(employeeList);
        when(employeeRepository.findByDepartmentId(anyLong())).thenReturn(ofResult);

        List<Employee> actualDepartmentById = employeeService.getDepartmentById(1L);

        verify(employeeRepository).findByDepartmentId(eq(1L));
        assertTrue(actualDepartmentById.isEmpty());
        assertSame(employeeList, actualDepartmentById);
    }

    @Test
    public void shouldGetDepartmentByIdThrowsException() throws Exception {
        Optional<List<Employee>> emptyResult = Optional.empty();
        when(employeeRepository.findByDepartmentId(anyLong())).thenReturn(emptyResult);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> employeeService.getDepartmentById(1L));
        verify(employeeRepository).findByDepartmentId(eq(1L));
    }

    @Test
    public void shouldDeleteAllByIdInBatch() throws Exception {
        doNothing().when(employeeRepository).deleteAllByIdInBatch(Mockito.<Iterable<Long>>any());
        employeeService.deleteAllByIdInBatch(new ArrayList<>());
        assertTrue(employeeService.getAllEmployees().isEmpty());
    }

    @Test
    public void shouldAddEmployeeList() {
        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest("Karthick", "Karthick",
                "Mahi@example.org", "6625550144", Gender.MALE, 20, 10.0d, Role.ADMIN, "Chennai", 1L);
        AddEmployeeRequest addEmployeeRequest2 = new AddEmployeeRequest("Mahi", "Karthick",
                "Mahi@example.org", "6625550144", Gender.FEMALE, 20, 10.0d, Role.USER, "Chennai", 1L);

        ArrayList<AddEmployeeRequest> addEmployeeRequestList = new ArrayList<>();
        addEmployeeRequestList.add(addEmployeeRequest);
        addEmployeeRequestList.add(addEmployeeRequest2);

        when(employeeRepository.saveAll(Mockito.<Iterable<Employee>>any())).thenReturn(new ArrayList<>());

        employeeService.addEmployeeList(addEmployeeRequestList);
        assertTrue(employeeService.getAllEmployees().isEmpty());
        verify(employeeRepository).saveAll(isA(Iterable.class));
    }

    @Test
    public void shouldValidateEmployeesBeforeAdding() {
        Optional<Department> emptyResult = Optional.empty();
        when(departmentRepository.findById(1L)).thenReturn(emptyResult);
        AddEmployeeRequest addEmployeeRequest = mock(AddEmployeeRequest.class);
        when(addEmployeeRequest.getFirstName()).thenReturn("Mahi");
        when(addEmployeeRequest.getDepartmentId()).thenReturn(1L);

        ArrayList<AddEmployeeRequest> addEmployeeRequestList = new ArrayList<>();
        addEmployeeRequestList.add(addEmployeeRequest);

        String actualValidateEmployeesBeforeAddingResult = employeeService
                .validateEmployeesBeforeAdding(addEmployeeRequestList);

        verify(addEmployeeRequest, atLeast(1)).getDepartmentId();
        verify(addEmployeeRequest).getFirstName();
        verify(departmentRepository).findById(eq(1L));
        assertEquals("For Employee Mahi department 1 is not present.\r\n", actualValidateEmployeesBeforeAddingResult);
    }

    @Test
    public void shouldValidateEmployeesBeforeAdding2() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        Optional<Department> ofResult = Optional.of(department);
        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        AddEmployeeRequest addEmployeeRequest = mock(AddEmployeeRequest.class);
        when(addEmployeeRequest.getDepartmentId()).thenReturn(1L);

        ArrayList<AddEmployeeRequest> addEmployeeRequestList = new ArrayList<>();
        addEmployeeRequestList.add(addEmployeeRequest);

        String actualValidateEmployeesBeforeAddingResult = employeeService
                .validateEmployeesBeforeAdding(addEmployeeRequestList);

        verify(addEmployeeRequest).getDepartmentId();
        verify(departmentRepository).findById(eq(1L));
        assertEquals("", actualValidateEmployeesBeforeAddingResult);
    }

    @Test
    public void shouldValidateEmployeesBeforeUpdating() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        Optional<Department> ofResult = Optional.of(department);
        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getDepartmentId()).thenReturn(1L);

        ArrayList<UpdateEmployeeRequest> updateEmployeeRequestList = new ArrayList<>();
        updateEmployeeRequestList.add(updateEmployeeRequest);

        String actualValidateEmployeesBeforeUpdatingResult = employeeService
                .validateEmployeesBeforeUpdating(updateEmployeeRequestList);

        verify(updateEmployeeRequest).getDepartmentId();
        verify(departmentRepository).findById(eq(1L));
        assertEquals("", actualValidateEmployeesBeforeUpdatingResult);
    }
    @Test
    public void testValidateEmployeesBeforeUpdating2() {
        Optional<Department> emptyResult = Optional.empty();
        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);
        UpdateEmployeeRequest updateEmployeeRequest = mock(UpdateEmployeeRequest.class);
        when(updateEmployeeRequest.getFirstName()).thenReturn("Mahi");
        when(updateEmployeeRequest.getDepartmentId()).thenReturn(1L);

        ArrayList<UpdateEmployeeRequest> updateEmployeeRequestList = new ArrayList<>();
        updateEmployeeRequestList.add(updateEmployeeRequest);

        String actualValidateEmployeesBeforeUpdatingResult = employeeService
                .validateEmployeesBeforeUpdating(updateEmployeeRequestList);

        verify(updateEmployeeRequest, atLeast(1)).getDepartmentId();
        verify(updateEmployeeRequest).getFirstName();
        verify(departmentRepository).findById(eq(1L));
        assertEquals("For Employee Mahi department 1 is not present\r\n", actualValidateEmployeesBeforeUpdatingResult);
    }
    @Test
    public void shouldUpdateEmployeeList() {
        when(employeeRepository.saveAll(Mockito.<Iterable<Employee>>any())).thenReturn(new ArrayList<>());

        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest(1L, "Mahi", "Karthick", "Mahi@example.org",
                "6625550144", Gender.FEMALE, 1, 10.0d, Role.ADMIN, "Chennai", 1L);
        updateEmployeeRequest.setId(2L);

        ArrayList<UpdateEmployeeRequest> updateEmployeeRequestList = new ArrayList<>();
        updateEmployeeRequestList.add(updateEmployeeRequest);

        employeeService.updateEmployeeList(updateEmployeeRequestList);

        verify(employeeRepository).saveAll(isA(Iterable.class));
    }
    @Test
    public void shouldDeleteAllEmployees() {
        doNothing().when(employeeRepository).deleteAll();
        employeeService.deleteAllEmployees();
        verify(employeeRepository).deleteAll();
    }
}
