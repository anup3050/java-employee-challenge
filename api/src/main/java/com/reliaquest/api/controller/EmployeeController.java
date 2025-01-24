package com.reliaquest.api.controller;

import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.service.IEmployeeService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class EmployeeController implements IEmployeeController<EmployeeResponse, EmployeeRequest> {

    private final IEmployeeService employeeService;

    @Override
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<EmployeeResponse>> getEmployeesByNameSearch(String searchString) {
        return new ResponseEntity<>(employeeService.getEmployeesByNameSearch(searchString), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeeById(String id) {

        return new ResponseEntity<EmployeeResponse>(
                (EmployeeResponse) employeeService.getEmployeeById(UUID.fromString(id)), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {

        return new ResponseEntity<>(employeeService.getHighestSalaryOfEmployees(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return new ResponseEntity<List<String>>(employeeService.getTopTenHighestEarningEmployeeNames(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid EmployeeRequest employeeRequest) {
        EmployeeResponse employeeResponse = (EmployeeResponse) employeeService.createEmployee(employeeRequest);
        return new ResponseEntity<>(employeeResponse, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {

        return new ResponseEntity<>(employeeService.deleteEmployee(id), HttpStatus.OK);
    }
}
