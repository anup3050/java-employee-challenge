package com.reliaquest.api.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reliaquest.api.MockEmployeeData;
import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;
import java.util.List;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeApiClient employeeApiClient;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockEmployeeData mockEmployeeData;

    @Test
    void shouldReturnAllEmployeeList() throws Exception {
        mockEmployeeData = new MockEmployeeData();
        when(employeeService.getAllEmployees()).thenReturn(mockEmployeeData.getMockEmployees());

        mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].name").value("Barrie Stark"))
                .andExpect(jsonPath("$[0].salary").value(298434))
                .andExpect(jsonPath("$[0].age").value(55))
                .andExpect(jsonPath("$[0].title").value("Dynamic Retail Architect"))
                .andExpect(jsonPath("$[0].email").value("bitchip@company.com"));
    }

    @Test
    void shouldReturnEmptyEmployeeList() throws Exception {
        mockEmployeeData = new MockEmployeeData();
        when(employeeService.getAllEmployees()).thenReturn(List.of());

        mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldHandleServiceException() throws Exception {

        when(employeeService.getAllEmployees()).thenThrow(new RuntimeException("Service unavailable"));

        mockMvc.perform(MockMvcRequestBuilders.get("/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldSearchEmployeeByName() throws Exception {
        mockEmployeeData = new MockEmployeeData();
        when(employeeService.getEmployeesByNameSearch("Barrie Stark"))
                .thenReturn(mockEmployeeData.getMockDataSearchedEmployee());

        mockMvc.perform(MockMvcRequestBuilders.get("/search/Barrie Stark").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Barrie Stark"))
                .andExpect(jsonPath("$[0].salary").value(298434))
                .andExpect(jsonPath("$[0].age").value(55))
                .andExpect(jsonPath("$[0].title").value("Dynamic Retail Architect"))
                .andExpect(jsonPath("$[0].email").value("bitchip@company.com"));
    }

    @Test
    void shouldSearchEmployeeById() throws Exception {
        mockEmployeeData = new MockEmployeeData();
        when(employeeService.getEmployeeById(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7428")))
                .thenReturn(mockEmployeeData.getMockEmployeesById());

        mockMvc.perform(MockMvcRequestBuilders.get("/bf07f5b8-34f9-4fde-adab-7946f5db7428")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("Barrie Stark"))
                .andExpect(jsonPath("salary").value(298434))
                .andExpect(jsonPath("age").value(55))
                .andExpect(jsonPath("title").value("Dynamic Retail Architect"))
                .andExpect(jsonPath("email").value("bitchip@company.com"));
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("John Doe");
        employeeRequest.setSalary(50000);
        employeeRequest.setAge(30);
        employeeRequest.setTitle("Software Engineer");

        EmployeeResponse employeeResponse = EmployeeResponse.builder()
                .id(String.valueOf(UUID.randomUUID()))
                .name("John Doe")
                .salary(50000)
                .age(30)
                .title("Software Engineer")
                .email("abc@test.com")
                .build();

        when(employeeService.createEmployee(employeeRequest)).thenReturn(employeeResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void shouldDeleteEmployees() throws Exception {
        mockMvc.perform(delete("/abc").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

    @Test
    void shouldReturnHighestSalaryOfEmployees() throws Exception {
        mockEmployeeData = new MockEmployeeData();
        when(employeeService.getHighestSalaryOfEmployees())
                .thenReturn(mockEmployeeData.getMockListEmployeeModel().get(0).getEmployee_salary());
        String expectedSalary = "298434";
        mockMvc.perform(MockMvcRequestBuilders.get("/highestSalary").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedSalary)));
        ;
    }

    @Test
    void shouldReturnTopTenHighestEarningEmployeeNames() throws Exception {
        List<String> nameList = List.of("John", "Devid", "Sagar");
        when(employeeService.getTopTenHighestEarningEmployeeNames()).thenReturn(nameList);
        mockMvc.perform(MockMvcRequestBuilders.get("/topTenHighestEarningEmployeeNames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(3)));
    }
}
