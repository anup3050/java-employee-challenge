package com.reliaquest.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.reliaquest.api.MockEmployeeData;
import com.reliaquest.api.client.EmployeeApiClient;
import com.reliaquest.api.client.EmployeeDeleteResponse;
import com.reliaquest.api.client.EmployeeWrapper;
import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.mapper.EmployeeMapper;
import com.reliaquest.api.model.EmployeeModel;
import com.reliaquest.api.request.EmployeeRequest;
import com.reliaquest.api.response.EmployeeResponse;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeApiClient employeeApiClient;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private MockEmployeeData mockEmployeeData = new MockEmployeeData();

    @Test
    public void testGetAllEmployeeList_success() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(mockEmployeeData.getMockListEmployeeModel()));
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);
        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);
        when(employeeMapper.getEmployees(any())).thenReturn(mockEmployeeData.getMockEmployees());
        List<EmployeeResponse> employeeResponseModels = employeeService.getAllEmployees();

        assertEquals(3, employeeResponseModels.size());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    void checkHandleServiceException() throws Exception {
        when(employeeApiClient.getEmployeesJson()).thenThrow(new RuntimeException("External API error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.getAllEmployees();
        });

        assertEquals("External API error", exception.getMessage());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void checkEmployeeListIsEmpty_exception() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.empty());
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);

        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getAllEmployees());
        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testSearchEmployeeByName_success() {

        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(mockEmployeeData.getMockListEmployeeModel()));
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);

        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);
        when(employeeMapper.getEmployees(any())).thenReturn(mockEmployeeData.getMockDataSearchedEmployee());

        List<EmployeeResponse> employeeResponseModels = employeeService.getEmployeesByNameSearch("Barrie Stark");

        assertEquals(1, employeeResponseModels.size());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testSearchEmployeeByName_exception() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.empty());
        when(employeeApiClient.getEmployeesJson()).thenReturn(new ResponseEntity<>(employeeWrapper, HttpStatus.OK));

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeesByNameSearch("Barrie Stark"));
        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testSearchEmployeeById_success() {
        List<EmployeeModel> employeeModels = mockEmployeeData.getMockListEmployeeModel();
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(List.of(employeeModels.get(0))));

        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);

        when(employeeApiClient.getEmployeeById(any())).thenReturn(responseEntity);
        when(employeeMapper.toEmployee(any()))
                .thenReturn(mockEmployeeData.getMockDataSearchedEmployee().get(0));

        EmployeeResponse employeeResponse =
                employeeService.getEmployeeById(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7428"));

        assertEquals("bf07f5b8-34f9-4fde-adab-7946f5db7428", employeeResponse.getId());

        verify(employeeApiClient, times(1)).getEmployeeById(any());
    }

    @Test
    public void testSearchEmployeeById_exception() {
        List<EmployeeModel> employeeModels = mockEmployeeData.getMockListEmployeeModel();
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.empty());

        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);

        when(employeeApiClient.getEmployeeById(any())).thenReturn(responseEntity);

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(UUID.fromString("bf07f5b8-34f9-4fde-adab-7946f5db7428")));

        verify(employeeApiClient, times(1)).getEmployeeById(any());
    }

    @Test
    public void testCreateEmployee_success() {

        ResponseEntity<EmployeeWrapper> responseEntity =
                new ResponseEntity<>(mockEmployeeData.feignClientResponseData(), HttpStatus.OK);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("John Doe");
        employeeRequest.setSalary(50000);
        employeeRequest.setAge(30);
        employeeRequest.setTitle("Software Engineer");

        when(employeeApiClient.createEmployeeApi(employeeRequest)).thenReturn(responseEntity);
        when(employeeMapper.toEmployee(any()))
                .thenReturn(mockEmployeeData.getMockDataSearchedEmployee().get(0));

        EmployeeResponse employeeResponse = employeeService.createEmployee(employeeRequest);

        assertEquals("bf07f5b8-34f9-4fde-adab-7946f5db7428", employeeResponse.getId());

        verify(employeeApiClient, times(1)).createEmployeeApi(any());
    }

    @Test
    public void testCreateEmployee_exception() {

        ResponseEntity<EmployeeWrapper> responseEntity =
                new ResponseEntity<>(mockEmployeeData.feignClientResponseData(), HttpStatus.INTERNAL_SERVER_ERROR);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("John Doe");
        employeeRequest.setSalary(50000);
        employeeRequest.setAge(30);
        employeeRequest.setTitle("Software Engineer");

        when(employeeApiClient.createEmployeeApi(employeeRequest)).thenReturn(responseEntity);

        assertThrows(RuntimeException.class, () -> employeeService.createEmployee(employeeRequest));

        verify(employeeApiClient, times(1)).createEmployeeApi(any());
    }

    @Test
    public void testDeleteEmployee_success() {
        // Request and response of GET Employee API

        ResponseEntity<EmployeeWrapper> responseEntity1 =
                new ResponseEntity<>(mockEmployeeData.feignClientResponseData(), HttpStatus.OK);

        when(employeeApiClient.getEmployeeById(any())).thenReturn(responseEntity1);

        // Request and response of DELETE Employee API
        EmployeeDeleteResponse employeeDeleteResponse = new EmployeeDeleteResponse();
        employeeDeleteResponse.setData(true);
        ResponseEntity<EmployeeDeleteResponse> responseEntity =
                new ResponseEntity<>(employeeDeleteResponse, HttpStatus.OK);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("Barrie Stark");

        when(employeeApiClient.deleteEmployeeAPI(any(EmployeeRequest.class))).thenReturn(responseEntity);

        String name = employeeService.deleteEmployee("bf07f5b8-34f9-4fde-adab-7946f5db7428");

        assertEquals("Barrie Stark", name);

        verify(employeeApiClient, times(1)).deleteEmployeeAPI(employeeRequest);
    }

    @Test
    public void testDeleteEmployee_getEmployeeById_exception() {
        List<EmployeeModel> employeeModels = mockEmployeeData.getMockListEmployeeModel();
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(List.of(employeeModels.get(0))));
        ResponseEntity<EmployeeWrapper> responseEntity1 =
                new ResponseEntity<>(employeeWrapper, HttpStatus.INTERNAL_SERVER_ERROR);

        EmployeeDeleteResponse employeeDeleteResponse = new EmployeeDeleteResponse();
        employeeDeleteResponse.setData(true);
        ResponseEntity<EmployeeDeleteResponse> responseEntity =
                new ResponseEntity<>(employeeDeleteResponse, HttpStatus.OK);

        when(employeeApiClient.getEmployeeById(any())).thenReturn(responseEntity1);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("John Doe");

        assertThrows(
                RuntimeException.class, () -> employeeService.deleteEmployee("bf07f5b8-34f9-4fde-adab-7946f5db7428"));
    }

    @Test
    public void testDeleteEmployee_deleteAPI_exception() {
        // Request and response of GET Employee API

        ResponseEntity<EmployeeWrapper> responseEntity1 =
                new ResponseEntity<>(mockEmployeeData.feignClientResponseData(), HttpStatus.OK);

        when(employeeApiClient.getEmployeeById(any())).thenReturn(responseEntity1);

        // Request and response of DELETE Employee API
        EmployeeDeleteResponse employeeDeleteResponse = new EmployeeDeleteResponse();
        employeeDeleteResponse.setData(true);
        ResponseEntity<EmployeeDeleteResponse> responseEntity =
                new ResponseEntity<>(employeeDeleteResponse, HttpStatus.INTERNAL_SERVER_ERROR);

        EmployeeRequest employeeRequest = new EmployeeRequest();
        employeeRequest.setName("Barrie Stark");

        when(employeeApiClient.deleteEmployeeAPI(any(EmployeeRequest.class))).thenReturn(responseEntity);

        assertThrows(
                RuntimeException.class, () -> employeeService.deleteEmployee("bf07f5b8-34f9-4fde-adab-7946f5db7428"));

        verify(employeeApiClient, times(1)).deleteEmployeeAPI(employeeRequest);
    }

    @Test
    public void testHighestSalary_success() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(mockEmployeeData.getMockListEmployeeModel()));
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);
        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);

        Integer salary = employeeService.getHighestSalaryOfEmployees();

        assertEquals(312289, salary);

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testHighestSalary_exception() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.empty());
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);
        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getHighestSalaryOfEmployees());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testTopTenHighestSalary_success() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.of(mockEmployeeData.getMockListEmployeeModelLarge()));
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);
        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);

        List<String> employeeNames = employeeService.getTopTenHighestEarningEmployeeNames();

        assertEquals(10, employeeNames.size());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }

    @Test
    public void testTopTenHighestSalary_exception() {
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setData(Optional.empty());
        ResponseEntity<EmployeeWrapper> responseEntity = new ResponseEntity<>(employeeWrapper, HttpStatus.OK);
        when(employeeApiClient.getEmployeesJson()).thenReturn(responseEntity);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getTopTenHighestEarningEmployeeNames());

        verify(employeeApiClient, times(1)).getEmployeesJson();
    }
}
