package com.reliaquest.api.service;

import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import java.util.List;
import java.util.UUID;

public interface IEmployeeService<Employee, Input> {

    List<EmployeeResponse> getAllEmployees();

    List<EmployeeResponse> getEmployeesByNameSearch(String searchString);

    EmployeeResponse getEmployeeById(UUID id);

    Integer getHighestSalaryOfEmployees();

    List<String> getTopTenHighestEarningEmployeeNames();

    EmployeeResponse createEmployee(EmployeeRequest employeeRequest);

    String deleteEmployee(String id);
}
