package com.reliaquest.api.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.reliaquest.api.model.EmployeeModel;
import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class EmployeeWrapper {

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    @JsonProperty("data")
    private Optional<List<EmployeeModel>> data;

    private String status;

    /*  public Optional<List<EmployeeModel>> getData() {
        return data;
    }*/
}
