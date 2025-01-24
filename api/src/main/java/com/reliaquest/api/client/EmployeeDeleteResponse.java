package com.reliaquest.api.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeDeleteResponse {

    @JsonProperty("data")
    private Boolean data;

    @JsonProperty("status")
    private String status;
}
