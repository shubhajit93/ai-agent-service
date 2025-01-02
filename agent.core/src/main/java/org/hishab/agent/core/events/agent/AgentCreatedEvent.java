package org.hishab.agent.core.events.agent;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;
import org.hishab.agent.core.model.agent.Agent;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Revision("1.0")
public class AgentCreatedEvent {
    String id;
    @NotNull
    Agent agent;
}
