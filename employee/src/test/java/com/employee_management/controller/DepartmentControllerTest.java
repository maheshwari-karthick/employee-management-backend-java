package com.employee_management.controller;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.model.Department;
import com.employee_management.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class DepartmentControllerTest {
    @Autowired
    private DepartmentController departmentController;

    @MockBean
    private DepartmentService departmentService;


    @Test
    public void shouldGetAllDepartments() throws Exception {
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        ArrayList<Department> departmentList = new ArrayList<>();
        departmentList.add(department);
        when(departmentService.getAllDepartments()).thenReturn(departmentList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/department/all");

        MockMvcBuilders.standaloneSetup(departmentController).build()
                .perform(requestBuilder).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":1,\"departmentName\":\"IT\",\"departmentPhone\":\"9825550144\",\"departmentEmail\"" + ":\"Mahi@example.org\",\"organizationName\":\"Google\",\"organizationAddress\":\"Chennai\",\"departmentHead\":\"Mahi\"}]"));
    }


    @Test
    public void shouldGetDepartmentById() throws Exception {

        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentService.getDepartmentById(Mockito.<Long>any())).thenReturn(department);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/department/{departmentId}", 1L);

        MockMvcBuilders.standaloneSetup(departmentController).build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"departmentName\":\"IT\",\"departmentPhone\":\"9825550144\",\"departmentEmail\"" + ":\"Mahi@example.org\",\"organizationName\":\"Google\",\"organizationAddress\":\"Chennai\",\"departmentHead\":\"Mahi\"}"));
    }

    @Test
    public void shouldAddNewDepartment() throws Exception {
        when(departmentService.addDepartment(Mockito.<Department>any())).thenReturn(1L);

        AddDepartmentRequest addDepartmentRequest = new AddDepartmentRequest("IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        String content = (new ObjectMapper()).writeValueAsString(addDepartmentRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/department").contentType(MediaType.APPLICATION_JSON).content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(departmentController).build().perform(requestBuilder);

        actualPerformResult
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Department added successfully.DepartmentId : 1"));
    }

    @Test
    public void shouldUpdateDepartment() throws Exception {

        when(departmentService.updateDepartment(Mockito.<Department>any())).thenReturn(1L);

        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        String content = (new ObjectMapper()).writeValueAsString(department);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/department").contentType(MediaType.APPLICATION_JSON).content(content);

        MockMvcBuilders.standaloneSetup(departmentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Department updated successfully.DepartmentId : 1"));
    }

    @Test
    public void shouldDeleteDepartment() throws Exception {
        // Arrange
        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentService.getDepartmentById(Mockito.<Long>any())).thenReturn(department);
        doNothing().when(departmentService).deleteDepartmentById(Mockito.<Long>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/department/{departmentId}", 1L);

        MockMvcBuilders.standaloneSetup(departmentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Department deleted successfully.DepartmentId : 1"))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"));
    }

    @Test
    public void shouldDeleteDepartmentReturnNotFoundException() throws Exception {

        Department department = new Department(1l, "IT", "9825550144", "Mahi@example.org", "Google", "Chennai", "Mahi");

        when(departmentService.getDepartmentById(Mockito.<Long>any())).thenReturn(department);
        doNothing().when(departmentService).deleteDepartmentById(Mockito.<Long>any());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(departmentController).build().perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
