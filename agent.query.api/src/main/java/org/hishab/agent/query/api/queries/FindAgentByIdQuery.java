package org.hishab.agent.query.api.queries;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindAgentByIdQuery {
    private String id;

    public String getId() {
        return id;
    }

}
