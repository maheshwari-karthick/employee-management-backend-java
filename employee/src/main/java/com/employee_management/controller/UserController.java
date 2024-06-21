package com.employee_management.controller;

import com.employee_management.Request.CreateUserRequest;
import com.employee_management.Request.UpdateUserRequest;
import com.employee_management.model.User;
import com.employee_management.response.GetUserResponse;
import com.employee_management.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/all")
    public ResponseEntity<List<GetUserResponse>> getAllUsers() {
        log.info("Get all Users");
        return new ResponseEntity<>(userService.findAllUser(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@Valid @RequestBody CreateUserRequest createUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            log.error("BindingError:" + result.getAllErrors());
            return new ResponseEntity<>(result.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        log.info("Add User : " + createUserRequest.toString());
        if (userService.existsUser(createUserRequest.getUserName())) {
            log.error("User already exists");
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
        long userId = userService.addUser(createUserRequest.toUser());
        log.info("new User got created : " + userId);
        return new ResponseEntity<>("new User got created : " + userId, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetUserResponse> getUserById(@PathVariable long userId) throws Exception {
        log.info("Get User By Id : " + userId);
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Binding Error:" + result.getAllErrors());
            return new ResponseEntity<>(result.getAllErrors().get(0).getDefaultMessage(), HttpStatus.BAD_REQUEST);
        }
        if (userService.existsUser(updateUserRequest.getUserName())) {
            Optional<User> user = userService.findUserByName(updateUserRequest.getUserName());
            if (user.isPresent() && user.get().getId() != updateUserRequest.getId()) {
                log.error("User already exists");
                return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
            }
        }
        log.info("Update User : " + updateUserRequest.toString());
        long userId = userService.updateUser(updateUserRequest.toUser());
        log.info("User got updated : " + userId);
        return new ResponseEntity<>("User got Updated : " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable long userId) throws Exception {
        log.info("Delete User By Id : " + userId);
        userService.findUserById(userId);
        userService.deleteUser(userId);
        return new ResponseEntity<>("User got Deleted : " + userId, HttpStatus.OK);
    }

    @DeleteMapping("/user/all")
    public ResponseEntity<String> deleteAllUsers() {
        log.info("Delete All Users");
        userService.deleteAllUsers();
        return new ResponseEntity<>("All the users are deleted", HttpStatus.OK);
    }


}
