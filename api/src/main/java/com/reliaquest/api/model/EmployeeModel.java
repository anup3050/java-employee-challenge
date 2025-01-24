package com.reliaquest.api.model;

import jakarta.validation.constraints.*;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeModel {

    private UUID id;
    private String employee_name;
    private Integer employee_salary;
    private Integer employee_age;
    private String employee_title;
    private String employee_email;
}
