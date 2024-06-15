package com.employee_management.controller;

import com.employee_management.Request.CreateUserRequest;
import com.employee_management.model.User;
import com.employee_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    private Logger log = Logger.getLogger(UserController.class.getName());

    @GetMapping("/user/all")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("Get all Users");
        return new ResponseEntity<>(userService.findAllUser(), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> addUser(@RequestBody CreateUserRequest createUserRequest) {
        log.info("Add User : " + createUserRequest);
        long userId = userService.addUser(createUserRequest.toUser());
        log.info("new User got created : " + userId);
        return new ResponseEntity<>("new User got created : " + userId, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable long userId) throws Exception {
        log.info("Get User By Id : " + userId);
        return new ResponseEntity<>(userService.findUserById(userId), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        log.info("Update User : " + user.toString());
        long userId = userService.updateUser(user);
        return new ResponseEntity<>("User got Updated : " + userId, HttpStatus.OK);
    }

    @GetMapping("/user/gettoken/{userName}")
    public ResponseEntity<String> generateTokenByAuthentication(@PathVariable String userName) throws Exception {
        log.info("generateTokenByAuthentication for user");
        return new ResponseEntity<>(userService.generateTokenByAuthentication(userName), HttpStatus.OK);
    }

}
