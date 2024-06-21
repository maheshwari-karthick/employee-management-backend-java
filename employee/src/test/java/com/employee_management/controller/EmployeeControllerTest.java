package com.employee_management.controller;

import com.employee_management.Request.AddEmployeeRequest;
import com.employee_management.Request.UpdateEmployeeRequest;
import com.employee_management.model.Employee;
import com.employee_management.model.Gender;
import com.employee_management.model.Role;
import com.employee_management.service.DepartmentService;
import com.employee_management.service.EmployeeService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {
    @Autowired
    private EmployeeController employeeController;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void shouldGetAllEmployees() throws Exception {
        Employee employee1 = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);
        Employee employee2 = new Employee(2, "Test", "Test", "test@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        ArrayList<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee2);
        when(employeeService.getAllEmployees()).thenReturn(employeeList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/all");

        MvcResult result = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(jsonPath("$.size()").value(2))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        List<Employee> employees = objectMapper.readValue(responseBody, new TypeReference<List<Employee>>() {
        });
        assertEquals(employeeList.toString(), employees.toString());
    }

    @Test
    public void shouldGetEmployeeById() throws Exception {
        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/{employeeId}", 1L);

        MvcResult result = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        Employee resultEmployee = objectMapper.readValue(responseBody, new TypeReference<Employee>() {
        });
        assertEquals(employee.toString(), resultEmployee.toString());
    }

    @Test
    public void shouldAddEmployee() throws Exception {

        when(departmentService.existsDepartmentById(Mockito.<Long>any())).thenReturn(false);
        when(employeeService.addEmployee(Mockito.<Employee>any())).thenReturn(1L);

        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest("Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        String content = (new ObjectMapper()).writeValueAsString(addEmployeeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee with id 1 added"));
    }

    @Test
    public void shouldAddEmployeeThrowsException() throws Exception {
        when(departmentService.existsDepartmentById(Mockito.<Long>any())).thenReturn(true);

        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest("Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        String content = (new ObjectMapper()).writeValueAsString(addEmployeeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Department does not exists"));
    }

    @Test
    public void shouldUpdateEmployee() throws Exception {
        when(employeeService.updateEmployee(Mockito.<Employee>any())).thenReturn(1L);
        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest(1l, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);
        String content = (new ObjectMapper()).writeValueAsString(updateEmployeeRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee with id 1 updated"));

    }

    @Test
    public void shouldUpdateEmployeeThrowsException() throws Exception {

        when(departmentService.existsDepartmentById(Mockito.<Long>any())).thenReturn(true);

        UpdateEmployeeRequest updateEmployeeRequest = new UpdateEmployeeRequest(1l, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        String content = (new ObjectMapper()).writeValueAsString(updateEmployeeRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Department does not exists"));
    }


    @Test
    public void shouldDeleteEmployee() throws Exception {
        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee);
        doNothing().when(employeeService).deleteEmployee(anyLong());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/employee/{employeeId}", 1L);

        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee with id 1 deleted"));
    }


    @Test
    public void shouldDeleteEmployeeReturnsException() throws Exception {
        Employee employee = new Employee(1, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000, Role.ADMIN, "address", 1);

        when(employeeService.getEmployeeById(anyLong())).thenReturn(employee);
        doNothing().when(employeeService).deleteEmployee(anyLong());
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders.formLogin();

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteEmployeeList() throws Exception {
        doNothing().when(employeeService).deleteAllEmployees();
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/employee/all");

        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string(" All the Employees are Deleted"));
    }

    @Test
    public void shouldUpdateEmployeeList() throws Exception {
        doNothing().when(employeeService).updateEmployeeList(Mockito.<List<UpdateEmployeeRequest>>any());
        when(employeeService.validateEmployeesBeforeUpdating(Mockito.<List<UpdateEmployeeRequest>>any())).thenReturn("");

        UpdateEmployeeRequest updateEmployeeRequest1 = new UpdateEmployeeRequest(1l, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);
        UpdateEmployeeRequest updateEmployeeRequest2 = new UpdateEmployeeRequest(2l, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        ArrayList<UpdateEmployeeRequest> updateEmployeeRequestList = new ArrayList<>();
        updateEmployeeRequestList.add(updateEmployeeRequest1);
        updateEmployeeRequestList.add(updateEmployeeRequest2);

        String content = (new ObjectMapper()).writeValueAsString(updateEmployeeRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employees Updated"));
    }

    @Test
    public void shouldUpdateEmployeeListThrowsException() throws Exception {
        doNothing().when(employeeService).updateEmployeeList(Mockito.<List<UpdateEmployeeRequest>>any());
        when(employeeService.validateEmployeesBeforeUpdating(Mockito.<List<UpdateEmployeeRequest>>any())).thenReturn("");

        UpdateEmployeeRequest updateEmployeeRequest1 = new UpdateEmployeeRequest(1l, "Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);
        UpdateEmployeeRequest updateEmployeeRequest2 = new UpdateEmployeeRequest(2l, "", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);


        ArrayList<UpdateEmployeeRequest> updateEmployeeRequestList = new ArrayList<>();
        updateEmployeeRequestList.add(updateEmployeeRequest1);
        updateEmployeeRequestList.add(updateEmployeeRequest2);
        String content = (new ObjectMapper()).writeValueAsString(updateEmployeeRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("firstName should not be blank."));
    }

    @Test
    public void shouldAddEmployeeListThrowsException() throws Exception {
        doNothing().when(employeeService).addEmployeeList(Mockito.<List<AddEmployeeRequest>>any());
        when(employeeService.validateEmployeesBeforeAdding(Mockito.<List<AddEmployeeRequest>>any())).thenReturn("");

        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest("Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);
        AddEmployeeRequest addEmployeeRequest1 = new AddEmployeeRequest("", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        ArrayList<AddEmployeeRequest> addEmployeeRequestList = new ArrayList<>();
        addEmployeeRequestList.add(addEmployeeRequest1);
        addEmployeeRequestList.add(addEmployeeRequest);

        String content = (new ObjectMapper()).writeValueAsString(addEmployeeRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("firstName should not be blank."));
    }

    @Test
    public void shouldAddEmployeeList() throws Exception {

        doNothing().when(employeeService).addEmployeeList(Mockito.<List<AddEmployeeRequest>>any());
        when(employeeService.validateEmployeesBeforeAdding(Mockito.<List<AddEmployeeRequest>>any())).thenReturn("");

        AddEmployeeRequest addEmployeeRequest = new AddEmployeeRequest("Mahi", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);
        AddEmployeeRequest addEmployeeRequest1 = new AddEmployeeRequest("Test", "Karthick", "mahi@example.com", "9698374323", Gender.FEMALE, 20, 2000d, Role.ADMIN, "address", 1L);

        ArrayList<AddEmployeeRequest> addEmployeeRequestList = new ArrayList<>();
        addEmployeeRequestList.add(addEmployeeRequest);
        addEmployeeRequestList.add(addEmployeeRequest1);

        String content = (new ObjectMapper()).writeValueAsString(addEmployeeRequestList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);


        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employees Added"));
    }

    @Test
    public void shouldDeleteEmployees() throws Exception {
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.delete("/employees")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new ArrayList<>()));

        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(employeeController)
                .build()
                .perform(requestBuilder);

        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Employee Ids are empty"));
    }
}
