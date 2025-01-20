package com.reliaquest.api.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IEmployeeService {


    List<Entity> getAllEmployees();


    List<Entity> getEmployeesByNameSearch( String searchString);


    Entity getEmployeeById( String id);


    Integer getHighestSalaryOfEmployees();


    List<String> getTopTenHighestEarningEmployeeNames();


    Entity createEmployee( Input employeeInput);


    String deleteEmployeeById( String id);
}
