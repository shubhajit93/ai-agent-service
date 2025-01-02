package org.hishab.agent.query.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hishab.agent.core.dto.Response;
import org.hishab.agent.core.model.stt.Stt;

@Data
@AllArgsConstructor
public class SttResponse implements Response<Stt> {
    @JsonProperty("data")
    private Stt data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    private String errorDetails;
}
