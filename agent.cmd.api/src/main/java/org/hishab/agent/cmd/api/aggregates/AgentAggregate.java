package org.hishab.agent.cmd.api.aggregates;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import org.hishab.agent.cmd.api.commands.CreateAgentCommand;
import org.hishab.agent.cmd.api.commands.DeleteAgentCommand;
import org.hishab.agent.cmd.api.commands.UpdateAgentCommand;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.events.agent.AgentCreatedEvent;
import org.hishab.agent.core.events.agent.AgentDeletedEvent;
import org.hishab.agent.core.events.agent.AgentUpdatedEvent;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.agent.Agent;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Aggregate(snapshotTriggerDefinition = "agentAggregateSnapshotTriggerDefinition")
@Slf4j
public class AgentAggregate {
    @AggregateIdentifier
    private String id;
    private Agent agent;

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Agent handle(CreateAgentCommand command) throws AgentException {
        if (command.getId() == null || command.getId().isBlank()) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent ID cannot be null or empty", null, null);
        }
        if (command.getAgent() == null) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent details cannot be null", null, null);
        }
        AggregateLifecycle.apply(new AgentCreatedEvent(command.getId(), command.getAgent()));
        return command.getAgent();
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Agent handle(UpdateAgentCommand command) throws AgentException {
        if (command.getId() == null || command.getId().isBlank()) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent ID cannot be null or empty", null, null);
        }
        if (command.getAgent() == null) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent details cannot be null", null, null);
        }
        log.info("agent event details for update {}", command.getAgent());

        AggregateLifecycle.apply(new AgentUpdatedEvent(command.getId(), command.getAgent()));
        return command.getAgent();
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public void handle(DeleteAgentCommand command) throws AgentException {
        if (command.getId() == null || command.getId().isBlank()) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent ID cannot be null or empty", null, null);
        }
        AggregateLifecycle.apply(new AgentDeletedEvent(command.getId(), command.getAgent()));
    }

    @EventSourcingHandler
    public void on(AgentCreatedEvent event) {
        this.id = event.getId();
        this.agent = event.getAgent();
    }

    @EventSourcingHandler
    public void on(AgentUpdatedEvent event) {
        this.id = event.getId();
        this.agent = event.getAgent();
    }

    @EventSourcingHandler
    public void on(AgentDeletedEvent event) {
        this.id = event.getId();
        this.agent = event.getAgent();
    }

}