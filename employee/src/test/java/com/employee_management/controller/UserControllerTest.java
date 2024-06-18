package com.employee_management.controller;

import com.employee_management.Request.CreateUserRequest;
import com.employee_management.Request.UpdateUserRequest;
import com.employee_management.model.Role;
import com.employee_management.model.User;
import com.employee_management.response.GetUserResponse;
import com.employee_management.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @Test
    public void shouldAddUser() throws Exception {
        when(userService.existsUser(Mockito.<String>any())).thenReturn(false);
        when(userService.addUser(Mockito.<User>any())).thenReturn(1L);

        CreateUserRequest createUserRequest = new CreateUserRequest("Mahi","Mahi", List.of(Role.ADMIN));

        String content = (new ObjectMapper()).writeValueAsString(createUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("new User got created : 1"));
    }
    @Test
    public void shouldNotAddUser() throws Exception {
        when(userService.existsUser(Mockito.<String>any())).thenReturn(true);
        when(userService.addUser(Mockito.<User>any())).thenReturn(1L);

        CreateUserRequest createUserRequest = new CreateUserRequest("Mahi","Mahi", List.of(Role.ADMIN));

        String content = (new ObjectMapper()).writeValueAsString(createUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().is(409))
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User already exists"));
    }

    @Test
   public void shouldUpdateUser() throws Exception {
        when(userService.updateUser(Mockito.<User>any())).thenReturn(1L);

        UpdateUserRequest updateUserRequest = new UpdateUserRequest(1l,"Mahi","Mahi", List.of(Role.ADMIN));

        String content = (new ObjectMapper()).writeValueAsString(updateUserRequest);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User got Updated : 1"));
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(new GetUserResponse(new User()));
        doNothing().when(userService).deleteUser(anyLong());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/{userId}", 1L);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("User got Deleted : 1"));
    }

    @Test
    public void shouldGetAllUsers() throws Exception {
        ArrayList<GetUserResponse> getUserResponseList = new ArrayList<>();
        getUserResponseList.add(new GetUserResponse(new User()));

        when(userService.findAllUser()).thenReturn(getUserResponseList);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/all");

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":0,\"username\":null,\"roles\":null}]"));
    }

    @Test
    public void shouldGetUserById() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(new GetUserResponse(new User()));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/{userId}", 1L);

        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"id\":0,\"username\":null,\"roles\":null}"));
    }
}
