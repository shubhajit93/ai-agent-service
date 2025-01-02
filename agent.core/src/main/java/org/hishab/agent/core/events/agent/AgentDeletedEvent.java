package org.hishab.agent.core.events.agent;

import lombok.*;
import org.axonframework.serialization.Revision;
import org.hishab.agent.core.model.agent.Agent;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@Value
@Revision("1.0")
public class AgentDeletedEvent {
    String id;
    Agent agent;
}
