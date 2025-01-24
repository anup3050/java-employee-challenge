package com.reliaquest.api.client;

import com.reliaquest.api.request.EmployeeRequest;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "mockEmployeeApi", url = "http://localhost:8112/api/v1/employee")
public interface EmployeeApiClient {

    @GetMapping()
    ResponseEntity<EmployeeWrapper> getEmployeesJson();

    @GetMapping("/{id}")
    ResponseEntity<EmployeeWrapper> getEmployeeById(@PathVariable("id") UUID id);

    @PostMapping
    ResponseEntity<EmployeeWrapper> createEmployeeApi(@RequestBody EmployeeRequest employeeRequest);

    @DeleteMapping
    ResponseEntity<EmployeeDeleteResponse> deleteEmployeeAPI(@RequestBody EmployeeRequest employeeRequest);
}
