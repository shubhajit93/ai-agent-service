package org.hishab.agent.query.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hishab.agent.core.dto.Response;
import org.hishab.agent.core.model.agent.Agent;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentResponse implements Response<Agent> {
    @JsonProperty("data")
    private Agent data;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_details")
    private Map<String, String> errorDetails;
}
