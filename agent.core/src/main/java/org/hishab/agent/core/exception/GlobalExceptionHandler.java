package org.hishab.agent.core.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.enums.ResponseStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(AgentException.class)
    public ResponseEntity<ApiResponse<Object>> handleAgentException(AgentException ex) {
        return ResponseEntity.status(ex.getErrorResponse().getStatusCode())
                .body(new ApiResponse<>(
                        ex.getData(),
                        ex.getMessage(),
                        ResponseStatus.ERROR.value,
                        ex.getErrorDetails()
                ));
    }

    @ExceptionHandler(AgentQueryException.class)
    public ResponseEntity<String> handleAgentQueryException(AgentQueryException ex) {
        try {
            String jsonResponse = objectMapper.writeValueAsString(ex);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            return ResponseEntity.status(ex.getErrorResponse().getStatusCode())
                    .headers(headers)
                    .body(jsonResponse);
        } catch (IOException e) {
            // Handle serialization error
            return ResponseEntity.status(500).body("{\"message\":\"Internal Server Error\"}");
        }
    }

}
