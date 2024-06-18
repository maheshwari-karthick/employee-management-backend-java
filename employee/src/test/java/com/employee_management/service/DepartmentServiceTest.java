package com.employee_management.service;

import com.employee_management.model.Department;
import com.employee_management.repository.DepartmentRepository;
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
class DepartmentServiceTest {
    @MockBean
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void shouldGetAllDepartments() {
        ArrayList<Department> departmentList = new ArrayList<>();
        when(departmentRepository.findAll()).thenReturn(departmentList);

        List<Department> actualAllDepartments = departmentService.getAllDepartments();

        verify(departmentRepository).findAll();
        assertTrue(actualAllDepartments.isEmpty());
        assertSame(departmentList, actualAllDepartments);
    }

    @Test
    public void shouldGetDepartmentById() throws Exception {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");
        Optional<Department> result = Optional.of(department);

        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(result);
        Department actualDepartmentById = departmentService.getDepartmentById(1L);

        verify(departmentRepository).findById(eq(1L));
        assertSame(department, actualDepartmentById);
    }

    @Test
    void shouldGetDepartmentByIdReturnException() throws Exception {

        Optional<Department> emptyResult = Optional.empty();
        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> departmentService.getDepartmentById(1L));
        verify(departmentRepository).findById(eq(1L));
    }

    @Test
    void shouldAddDepartment() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentRepository.save(Mockito.<Department>any())).thenReturn(department);

        long actualAddDepartmentResult = departmentService.addDepartment(department);

        verify(departmentRepository).save(isA(Department.class));
        assertEquals(1L, actualAddDepartmentResult);
    }

    @Test
    void shouldUpdateDepartment() {
        // Arrange

        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentRepository.save(Mockito.<Department>any())).thenReturn(department);

        long actualUpdateDepartmentResult = departmentService.updateDepartment(department);

        verify(departmentRepository).save(isA(Department.class));
        assertEquals(1L, actualUpdateDepartmentResult);
    }

    @Test
    void shouldDeleteDepartmentById() {
        doNothing().when(departmentRepository).deleteById(Mockito.<Long>any());
        departmentService.deleteDepartmentById(1L);
        verify(departmentRepository).deleteById(eq(1L));
        assertTrue(departmentService.getAllDepartments().isEmpty());
    }
}
