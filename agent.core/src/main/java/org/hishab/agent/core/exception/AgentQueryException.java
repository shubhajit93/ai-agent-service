package org.hishab.agent.core.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.model.agent.Agent;

import java.util.Map;

@Getter
public class AgentQueryException extends BaseException {

    @JsonProperty("error_details")
    private final Map<String, String> errorDetails;
    private final Agent data;

    public AgentQueryException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Throwable throwable, Agent data) {
        super(errorResponse, message, throwable);
        this.errorDetails = errorDetails;
        this.data = data;
    }

    public AgentQueryException(ErrorResponse errorResponse, String message, Map<String, String> errorDetails, Agent data) {
        super(errorResponse, message);
        this.errorDetails = errorDetails;
        this.data = data;
    }

}