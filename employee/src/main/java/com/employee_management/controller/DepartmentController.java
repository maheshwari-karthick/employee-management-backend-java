package com.employee_management.controller;

import com.employee_management.Request.AddDepartmentRequest;
import com.employee_management.Request.UpdateDepartmentRequest;
import com.employee_management.model.Department;
import com.employee_management.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/department/all")
    public ResponseEntity<List<Department>> getAllDepartments() {
        log.info("Get all department list");

        List<Department> departments = departmentService.getAllDepartments();

        log.info("Department List: " + departments.toString());
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable long departmentId) throws Exception {
        log.info("Get Department By Id : " + departmentId);

        Department department = departmentService.getDepartmentById(departmentId);

        log.info("Department : " + department.toString());
        return new ResponseEntity<>(department, HttpStatus.OK);
    }

    @PostMapping("/department")
    public ResponseEntity<String> addDepartment(@RequestBody @Valid AddDepartmentRequest addDepartmentRequest,
                                                BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            log.error("Binding Error in addDepartment : " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        if (departmentService.existsDepartmentByName(addDepartmentRequest.getDepartmentName())) {
            log.error("Department Name Already Exists");
            return new ResponseEntity<>("Department Name Already Exists", HttpStatus.CONFLICT);
        }

        log.info("Add Department : " + addDepartmentRequest.toString());
        long departmentId = departmentService.addDepartment(addDepartmentRequest.toDepartment());

        log.info("Department Id : " + departmentId);
        return new ResponseEntity<>("Department added successfully.DepartmentId : " + departmentId, HttpStatus.CREATED);
    }

    @PutMapping("/department")
    public ResponseEntity<String> updateDepartment(@RequestBody @Valid UpdateDepartmentRequest updateDepartmentRequest,
                                                   BindingResult bindingResult) throws Exception {
        log.info("Update Department : " + updateDepartmentRequest.toString());

        if (bindingResult.hasErrors()) {
            log.error("Binding Error : " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        if (departmentService.existsDepartmentByName(updateDepartmentRequest.getDepartmentName())) {
            Optional<Department> optionalDepartment = departmentService.getDepartmentByName(updateDepartmentRequest.getDepartmentName());
            if (optionalDepartment.isPresent() && optionalDepartment.get().getId() != updateDepartmentRequest.getId()) {
                log.error("Department Already Exists");
                return new ResponseEntity<>("Department Name Already Exists", HttpStatus.CONFLICT);
            }
        }

        long updatedDepartmentId = departmentService.updateDepartment(updateDepartmentRequest.toDepartment());

        log.info("Updated Department Id : " + updatedDepartmentId);
        return new ResponseEntity<>("Department updated successfully.DepartmentId : " + updatedDepartmentId, HttpStatus.OK);
    }

    @DeleteMapping("/department/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable long departmentId) throws Exception {
        log.info("Delete Department : " + departmentId);

        departmentService.getDepartmentById(departmentId);
        departmentService.deleteDepartmentById(departmentId);

        return new ResponseEntity<>("Department deleted successfully.DepartmentId : " + departmentId, HttpStatus.OK);
    }

    @PostMapping("/departments")
    public ResponseEntity<String> AddDepartmentList(@RequestBody @Valid List<AddDepartmentRequest> addDepartmentRequestList,
                                                    BindingResult bindingResult) throws Exception {
        log.info("Add Departments : " + addDepartmentRequestList.toString());

        if (bindingResult.hasErrors()) {
            log.error("BindingError : " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        List<String> departmentNames = addDepartmentRequestList.stream().map(AddDepartmentRequest::getDepartmentName).toList();
        String resultString = departmentService.existsDepartmentByNameListBeforeAdding(departmentNames);
        if (!resultString.isEmpty()) {
            log.error(resultString);
            return new ResponseEntity<>(resultString, HttpStatus.CONFLICT);
        }

        departmentService.addDepartmentList(addDepartmentRequestList);
        log.info("DepartmentList Added");
        return new ResponseEntity<>("DepartmentList Added", HttpStatus.CREATED);
    }


    @PutMapping("/departments")
    public ResponseEntity<String> updateDepartmentList(@RequestBody @Valid List<UpdateDepartmentRequest> updateDepartmentRequestList,
                                                       BindingResult bindingResult) throws Exception {
        log.info("Update Departments : " + updateDepartmentRequestList.toString());

        if (bindingResult.hasErrors()) {
            log.error("Binding Error  in updateDepartmentList : " + Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
            throw new BadRequestException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        String resultString = departmentService.existsDepartmentByNameListBeforeUpdating(updateDepartmentRequestList);
        if (!resultString.isEmpty()) {
            log.error(resultString);
            return new ResponseEntity<>(resultString, HttpStatus.CONFLICT);
        }

        departmentService.updateDepartmentList(updateDepartmentRequestList);
        log.info("DepartmentList Updated");
        return new ResponseEntity<>("DepartmentList Updated", HttpStatus.CREATED);
    }

    @DeleteMapping("/departments")
    public ResponseEntity<String> deleteDepartmentByBatchId(@RequestBody List<Long> departmentIds) throws Exception {
        log.info("Delete Departments : " + departmentIds.toString());

        if (departmentIds.isEmpty()) {
            log.error("Department Ids are Empty");
            return new ResponseEntity<>("Department Ids are Empty", HttpStatus.CONFLICT);
        }
        departmentService.deleteAllDepartmentsByIdInBatch(departmentIds);

        return new ResponseEntity<>("Departments deleted", HttpStatus.OK);
    }

    @DeleteMapping("/department/all")
    public ResponseEntity<String> deleteAllDepartments() throws Exception {
        log.info("Delete All Departments");
        departmentService.deleteAllDepartments();
        return new ResponseEntity<>("All Departments are deleted", HttpStatus.OK);
    }


}
