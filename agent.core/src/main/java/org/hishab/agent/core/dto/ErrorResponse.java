package org.hishab.agent.core.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public enum ErrorResponse {
    AUTHORIZATION_ERROR(401, "Unauthorized", "", null),
    VALIDATION_ERROR(422, "Validation Error", "", null),
    INTERNAL_ERROR(500, "Internal Server Error", "", null);

    private final int statusCode;
    private final String errorCode;
    private final String errorMessage;
    private final Map<String, String> errorDetails;

    ErrorResponse(int statusCode, String errorCode, String errorMessage, Map<String, String> errorDetails) {
        this.statusCode = statusCode;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetails = errorDetails;
    }

}
