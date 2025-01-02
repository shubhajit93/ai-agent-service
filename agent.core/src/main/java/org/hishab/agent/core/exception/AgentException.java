package org.hishab.agent.core.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.model.agent.Agent;

import java.util.Map;

@Getter
public class AgentException extends BaseException {

    @JsonProperty("error_details")
    private final Map<String, String> errorDetails;
    private final Object data;

    public AgentException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Throwable throwable, Object data) {
        super(errorResponse, message, throwable);
        this.errorDetails = errorDetails;
        this.data = data;
    }

    public AgentException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Object data) {
        super(errorResponse, message);
        this.errorDetails = errorDetails;
        this.data = data;
    }

    // Overloaded constructor for specific data type
    public AgentException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Throwable throwable, Agent data) {
        super(errorResponse, message, throwable);
        this.errorDetails = errorDetails;
        this.data = data;
    }

    public AgentException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Agent data) {
        super(errorResponse, message);
        this.errorDetails = errorDetails;
        this.data = data;
    }

}