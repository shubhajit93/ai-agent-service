package org.hishab.agent.query.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.dto.Response;
import org.hishab.agent.core.model.stt.Stt;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SttListResponse implements Response<List<Stt>> {
    @JsonProperty("data")
    private List<Stt> data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_details")
    private String errorDetails;
}
