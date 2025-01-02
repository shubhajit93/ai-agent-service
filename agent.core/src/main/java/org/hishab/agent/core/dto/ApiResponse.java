package org.hishab.agent.core.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ApiResponse<T> implements Response<T> {

    @Getter
    private T data;
    private String message;
    private String status;
    @Getter
    private Map<String, String> errorDetails;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String getStatus() {
        return this.status;
    }

}
