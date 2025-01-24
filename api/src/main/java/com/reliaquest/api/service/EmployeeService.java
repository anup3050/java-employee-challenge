package com.reliaquest.api.service;

import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.client.EmployeeDeleteResponse;
import com.reliaquest.api.client.EmployeeWrapper;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.model.EmployeeModel;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Slf4j
@Validated
public class EmployeeService implements IEmployeeService {

    private final EmployeeApiClient employeeApiClient;
    private final EmployeeMapper employeeMapper;

    @Override
    public List<EmployeeResponse> getAllEmployees() {

        try {
            List<EmployeeModel> employees = Objects.requireNonNull(
                            employeeApiClient.getEmployeesJson().getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Record not found!"));

            List<EmployeeResponse> employeesList = employeeMapper.getEmployees(employees);
            if (employeesList == null || employeesList.isEmpty())
                throw new EmployeeNotFoundException("Record not found!");
            return employeesList;
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee record not found while retrieving employed details: {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while retrieving employed details: {0}", exception);
            throw exception;
        }
    }

    @Override
    public List<EmployeeResponse> getEmployeesByNameSearch(String searchString) {
        try {
            if (searchString == null || searchString.isBlank())
                throw new RuntimeException("Invalid name it is either blank or null.");

            List<EmployeeModel> employeesModel = Objects.requireNonNull(
                            employeeApiClient.getEmployeesJson().getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found!"));

            return employeeMapper.getEmployees(employeesModel).stream()
                    .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                    .toList();
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee details not found while searching employee by name : {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while searching employee by name {0}", exception);
            throw exception;
        }
    }

    @Override
    public EmployeeResponse getEmployeeById(UUID id) {
        try {
            ResponseEntity<EmployeeWrapper> employeeModel = employeeApiClient.getEmployeeById(id);
            return employeeMapper.toEmployee(Objects.requireNonNull(employeeModel.getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found"))
                    .get(0));
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee details not found while searching employee by Id : {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while searching employee by Id {0}", exception);
            throw exception;
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployees() {
        try {
            return Objects.requireNonNull(employeeApiClient.getEmployeesJson().getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found"))
                    .stream()
                    .mapToInt(EmployeeModel::getEmployee_salary)
                    .max()
                    .orElseThrow(() -> new RuntimeException("No highest salary found"));
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee Highest Salary details not found : {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while retrieving highest salary: {0}", exception);
            throw exception;
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() {
        try {
            return Objects.requireNonNull(employeeApiClient.getEmployeesJson().getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found"))
                    .stream()
                    .sorted((e1, e2) -> Integer.compare(e1.getEmployee_salary(), e2.getEmployee_salary()))
                    .limit(10)
                    .map(EmployeeModel::getEmployee_name)
                    .toList();
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee top ten Highest Salary details not found : {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while retrieving top ten salary: {0}", exception);
            throw exception;
        }
    }

    @Override
    public EmployeeResponse createEmployee(@Valid EmployeeRequest employeeRequest) {
        try {
            ResponseEntity<EmployeeWrapper> responseEntity = employeeApiClient.createEmployeeApi(employeeRequest);

            if (!responseEntity.getStatusCode().is2xxSuccessful()) throw new RuntimeException("Employee not created!");

            return employeeMapper.toEmployee(Objects.requireNonNull(responseEntity.getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found"))
                    .get(0));
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("New employee details not found : {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while creating employee: {0}", exception);
            throw exception;
        }
    }

    @Override
    public String deleteEmployee(String id) {
        try {
            ResponseEntity<EmployeeWrapper> employeeModel = employeeApiClient.getEmployeeById(UUID.fromString(id));
            if (!employeeModel.getStatusCode().is2xxSuccessful()) throw new RuntimeException("Internal service error!");

            EmployeeRequest employeeRequest = new EmployeeRequest();
            String name = Objects.requireNonNull(employeeModel.getBody())
                    .getData()
                    .orElseThrow(() -> new EmployeeNotFoundException("Employee record not found"))
                    .get(0)
                    .getEmployee_name();
            employeeRequest.setName(name);
            ResponseEntity<EmployeeDeleteResponse> responseEntity =
                    employeeApiClient.deleteEmployeeAPI(employeeRequest);
            if (responseEntity.getStatusCode().is4xxClientError()
                    || responseEntity.getStatusCode().is5xxServerError()
                    || !Objects.requireNonNull(responseEntity.getBody()).getData())
                throw new RuntimeException("User delete failed!");

            return name;
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            log.error("Employee details not found for delete operation: {0}", employeeNotFoundException);
            throw employeeNotFoundException;
        } catch (RuntimeException exception) {
            log.error("Exception occurred while deleting employee: {0}", exception);
            throw exception;
        }
    }
}
