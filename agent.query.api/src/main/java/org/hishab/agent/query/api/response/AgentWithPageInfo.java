package org.hishab.agent.query.api.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.query.api.model.agent.AgentDocument;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgentWithPageInfo {
    List<AgentDocument> content;
    int totalPages;
    long totalElements;
}
