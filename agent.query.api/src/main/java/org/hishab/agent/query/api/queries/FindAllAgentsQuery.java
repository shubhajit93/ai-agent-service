package org.hishab.agent.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.AgentStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAllAgentsQuery {
    private List<String> agentIds;
    private AgentStatus agentStatus;
    private String sortDirection;
    private int pageSize;
    private String pageToken;
}