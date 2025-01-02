package org.hishab.agent.query.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hishab.agent.core.dto.Response;
import org.hishab.agent.core.model.tts.Tts;

import java.util.List;

@Data
@AllArgsConstructor
public class TtsListResponse implements Response<List<Tts>> {
    @JsonProperty("data")
    private List<Tts> data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_details")
    private String errorDetails;
} 