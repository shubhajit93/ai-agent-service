package org.hishab.agent.query.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.dto.Response;
import org.hishab.agent.core.model.agent.Agent;

import java.util.List;

@AllArgsConstructor
//@NoArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgentListResponse implements Response<List<Agent>> {
    @JsonProperty("data")
    private List<Agent> data;
    @JsonProperty("next_page_token")
    private String nextPageToken;
    @JsonProperty("message")
    private String message;
    @JsonProperty("status")
    private String status;
    @JsonProperty("error_details")
    private String errorDetails;
}
