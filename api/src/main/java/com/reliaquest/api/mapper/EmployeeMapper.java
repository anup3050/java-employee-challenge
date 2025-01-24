package com.reliaquest.api.mapper;

import com.reliaquest.api.model.EmployeeModel;
import com.reliaquest.api.response.EmployeeResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    @Mapping(source = "employee_name", target = "name")
    @Mapping(source = "employee_salary", target = "salary")
    @Mapping(source = "employee_age", target = "age")
    @Mapping(source = "employee_title", target = "title")
    @Mapping(source = "employee_email", target = "email")
    EmployeeResponse toEmployee(EmployeeModel externalEmployee);

    List<EmployeeResponse> getEmployees(List<EmployeeModel> carEntities);
}
