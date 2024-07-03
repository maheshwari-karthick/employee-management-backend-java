package com.employee_management.service;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.Request.UpdateDepartmentRequest;
import com.employee_management.model.Department;
import com.employee_management.repository.DepartmentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void shouldGetDepartmentByIdReturnException() throws Exception {

        Optional<Department> emptyResult = Optional.empty();
        when(departmentRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        assertThrows(ChangeSetPersister.NotFoundException.class, () -> departmentService.getDepartmentById(1L));
        verify(departmentRepository).findById(eq(1L));
    }

    @Test
    public void shouldAddDepartment() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentRepository.save(Mockito.<Department>any())).thenReturn(department);

        long actualAddDepartmentResult = departmentService.addDepartment(department);

        verify(departmentRepository).save(isA(Department.class));
        assertEquals(1L, actualAddDepartmentResult);
    }

    @Test
    public void shouldUpdateDepartment() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentRepository.save(Mockito.<Department>any())).thenReturn(department);

        long actualUpdateDepartmentResult = departmentService.updateDepartment(department);

        verify(departmentRepository).save(isA(Department.class));
        assertEquals(1L, actualUpdateDepartmentResult);
    }

    @Test
    public void shouldDeleteDepartmentById() {
        doNothing().when(departmentRepository).deleteById(Mockito.<Long>any());
        departmentService.deleteDepartmentById(1L);
        verify(departmentRepository).deleteById(eq(1L));
    }

    @Test
    public void shouldGetExistsDepartmentByName() {
        when(departmentRepository.existsByDepartmentName(Mockito.<String>any())).thenReturn(true);
        boolean actualExistsDepartmentByNameResult = departmentService.existsDepartmentByName("IT");
        verify(departmentRepository).existsByDepartmentName(eq("IT"));
        assertTrue(actualExistsDepartmentByNameResult);
    }

    @Test
    public void shouldGetDepartmentByName() {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        Optional<Department> ofResult = Optional.of(department);
        when(departmentRepository.findByDepartmentName(Mockito.<String>any())).thenReturn(ofResult);

        Optional<Department> actualDepartmentByName = departmentService.getDepartmentByName("IT");
        verify(departmentRepository).findByDepartmentName(eq("IT"));
        assertSame(ofResult, actualDepartmentByName);
    }

    @Test
    public void shouldExistsDepartmentById() {
        when(departmentRepository.existsById(Mockito.<Long>any())).thenReturn(false);
        boolean actualExistsDepartmentByIdResult = departmentService.existsDepartmentById(1L);
        verify(departmentRepository).existsById(eq(1L));
        assertTrue(actualExistsDepartmentByIdResult);
    }

    @Test
    public void shouldDeleteAllDepartments() {
        doNothing().when(departmentRepository).deleteAll();
        departmentService.deleteAllDepartments();
        verify(departmentRepository).deleteAll();
    }

    @Test
    public void shouldDeleteAllByIdInBatch() throws Exception {
        doNothing().when(departmentRepository).deleteAllByIdInBatch(Mockito.<Iterable<Long>>any());
        departmentService.deleteAllDepartmentsByIdInBatch(new ArrayList<>());
        assertTrue(departmentService.getAllDepartments().isEmpty());
    }

    @Test
    public void shouldExistsDepartmentByNameListBeforeAddingReturnsAlreadyPresent() {
        when(departmentRepository.existsByDepartmentName(Mockito.<String>any())).thenReturn(true);
        ArrayList<String> names = new ArrayList<>();
        names.add("Mahi");
        String actualExistsDepartmentByNameListBeforeAddingResult = departmentService
                .existsDepartmentByNameListBeforeAdding(names);
        verify(departmentRepository).existsByDepartmentName(eq("Mahi"));
        assertEquals("Department Name Mahi already present.\r\n", actualExistsDepartmentByNameListBeforeAddingResult);
    }

    @Test
    public void shouldExistsDepartmentByNameListBeforeAdding() {
        when(departmentRepository.existsByDepartmentName(Mockito.<String>any())).thenReturn(false);
        ArrayList<String> names = new ArrayList<>();
        names.add("Mahi");
        String actualExistsDepartmentByNameListBeforeAddingResult = departmentService
                .existsDepartmentByNameListBeforeAdding(names);
        verify(departmentRepository).existsByDepartmentName(eq("Mahi"));
        assertEquals("", actualExistsDepartmentByNameListBeforeAddingResult);
    }

    @Test
    public void shouldExistsDepartmentByNameListBeforeUpdatingReturnsAlreadyPresent() {
        Department department = new Department(1L, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");
        Optional<Department> ofResult = Optional.of(department);
        when(departmentRepository.findByDepartmentName(Mockito.<String>any())).thenReturn(ofResult);
        UpdateDepartmentRequest updateDepartmentRequest = new UpdateDepartmentRequest(2L, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        ArrayList<UpdateDepartmentRequest> updateDepartmentRequestList = new ArrayList<>();
        updateDepartmentRequestList.add(updateDepartmentRequest);

        String actualExistsDepartmentByNameListBeforeUpdatingResult = departmentService
                .existsDepartmentByNameListBeforeUpdating(updateDepartmentRequestList);

        assertEquals("Department Name IT already present.\r\n",
                actualExistsDepartmentByNameListBeforeUpdatingResult);
    }

    @Test
    public void shouldExistsDepartmentByNameListBeforeUpdating() {
        Department department = new Department(1L, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");
        Optional<Department> ofResult = Optional.of(department);
        when(departmentRepository.findByDepartmentName(Mockito.<String>any())).thenReturn(ofResult);
        UpdateDepartmentRequest updateDepartmentRequest = new UpdateDepartmentRequest(1L, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        ArrayList<UpdateDepartmentRequest> updateDepartmentRequestList = new ArrayList<>();
        updateDepartmentRequestList.add(updateDepartmentRequest);

        String actualExistsDepartmentByNameListBeforeUpdatingResult = departmentService
                .existsDepartmentByNameListBeforeUpdating(updateDepartmentRequestList);

        assertEquals("", actualExistsDepartmentByNameListBeforeUpdatingResult);
    }

    @Test
    public void shouldAddDepartmentToDepartmentList() {
        AddDepartmentRequest addDepartmentRequest = new AddDepartmentRequest("IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");
        List<AddDepartmentRequest> addDepartmentRequestList = new ArrayList<>();
        addDepartmentRequestList.add(addDepartmentRequest);
        departmentService.addDepartmentList(addDepartmentRequestList);
        verify(departmentRepository).save(isA(Department.class));
    }

    @Test
    public void shouldUpdateDepartmentList() {
        UpdateDepartmentRequest updateDepartmentRequest = new UpdateDepartmentRequest(1L,"IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");
        List<UpdateDepartmentRequest> updateDepartmentRequestList = new ArrayList<>();
        updateDepartmentRequestList.add(updateDepartmentRequest);
        departmentService.updateDepartmentList(updateDepartmentRequestList);
        verify(departmentRepository).save(isA(Department.class));
    }

}
