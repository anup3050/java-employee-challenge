package com.reliaquest.api;

import com.reliaquest.api.client.EmployeeWrapper;
import com.reliaquest.api.model.EmployeeModel;
import com.reliaquest.api.response.EmployeeResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MockEmployeeData {

    public List<EmployeeResponse> getMockEmployees() {
        List<EmployeeResponse> employeeResponses = new ArrayList<>();

        employeeResponses.add(EmployeeResponse.builder()
                .id("bf07f5b8-34f9-4fde-adab-7946f5db7428")
                .name("Barrie Stark")
                .salary(298434)
                .age(55)
                .title("Dynamic Retail Architect")
                .email("bitchip@company.com")
                .build());

        employeeResponses.add(EmployeeResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Nelson Hermann")
                .salary(312289)
                .age(32)
                .title("Future Accounting Director")
                .email("mcshayne@company.com")
                .build());

        employeeResponses.add(EmployeeResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Alecia Hessel PhD")
                .salary(210488)
                .age(46)
                .title("Internal Retail Associate")
                .email("lotstring@company.com")
                .build());

        return employeeResponses;
    }

    public List<EmployeeModel> getMockListEmployeeModel() {
        List<EmployeeModel> employees = new ArrayList<>();
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7428"))
                .employee_name("Barrie Stark")
                .employee_salary(298434)
                .employee_age(55)
                .employee_title("Dynamic Retail Architect")
                .employee_email("bitchip@company.com")
                .build());

        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Nelson Hermann")
                .employee_salary(312289)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());

        return employees;
    }

    public List<EmployeeModel> getMockEmployeesBySearch() {
        List<EmployeeModel> employeeModels = new ArrayList<>();

        // employeeModels.add(new EmployeeModel("1", "Barrie Stark", 298434, 55, "Dynamic Retail Architect",
        // "bitchip@company.com"));

        return employeeModels;
    }

    public EmployeeResponse getMockEmployeesById() {
        return getMockEmployees().get(0);
    }

    public List<EmployeeResponse> getMockDataSearchedEmployee() {
        List<EmployeeResponse> employeeResponses = new ArrayList<>();
        employeeResponses.add(EmployeeResponse.builder()
                .id("bf07f5b8-34f9-4fde-adab-7946f5db7428")
                .name("Barrie Stark")
                .salary(298434)
                .age(55)
                .title("Dynamic Retail Architect")
                .email("bitchip@company.com")
                .build());
        return employeeResponses;
    }

    public EmployeeWrapper feignClientResponseData() {
        List<EmployeeModel> employeeModels = getMockListEmployeeModel();
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(List.of(employeeModels.get(0))));

        return employeeWrapper;
    }

    public List<EmployeeModel> getMockListEmployeeModelLarge() {
        List<EmployeeModel> employees = new ArrayList<>();
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7428"))
                .employee_name("Barrie Stark")
                .employee_salary(298434)
                .employee_age(55)
                .employee_title("Dynamic Retail Architect")
                .employee_email("bitchip@company.com")
                .build());

        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Hermann")
                .employee_salary(30000)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Nelson")
                .employee_salary(312289)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Barrie")
                .employee_salary(312289)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Stark")
                .employee_salary(312289)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Stark Hermann")
                .employee_salary(233243)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Nelson Stark")
                .employee_salary(535333)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Nelson H")
                .employee_salary(212123)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Hermann Stark")
                .employee_salary(545454)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("N. Hermann")
                .employee_salary(232323)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Dr.Son Hermann")
                .employee_salary(23232)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());
        employees.add(EmployeeModel.builder()
                .id(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7430"))
                .employee_name("Dr.Hermann D")
                .employee_salary(434344)
                .employee_age(32)
                .employee_title("Future Accounting Director")
                .employee_email("mcshayne@company.com")
                .build());

        return employees;
    }
}
