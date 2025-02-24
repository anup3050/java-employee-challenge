package com.reliaquest.api.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {
    private String id;
    private String name;
    private int salary;
    private int age;
    private String title;
    private String email;
}
