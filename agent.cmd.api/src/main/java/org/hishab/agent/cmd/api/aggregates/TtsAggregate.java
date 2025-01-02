package org.hishab.agent.cmd.api.aggregates;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;
import org.hishab.agent.cmd.api.commands.CreateTtsCommand;
import org.hishab.agent.cmd.api.commands.UpdateTtsCommand;
import org.hishab.agent.core.events.tts.TtsCreatedEvent;
import org.hishab.agent.core.events.tts.TtsUpdatedEvent;
import org.hishab.agent.core.exception.TtsException;
import org.hishab.agent.core.model.tts.Tts;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Data
@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "agentAggregateSnapshotTriggerDefinition")
@Slf4j
public class TtsAggregate {

    @AggregateIdentifier
    private String id;
    private Tts tts;

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Tts handle(CreateTtsCommand command) {
        if (command.getTts() == null) {
            throw new TtsException("TtsDto details cannot be null");
        }
        if (command.getTts().getName() == null || command.getTts().getName().isBlank()) {
            throw new TtsException("TtsDto name cannot be null or empty");
        }
        apply(new TtsCreatedEvent(command.getId(), command.getTts()));
        return this.tts;
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Tts handle(UpdateTtsCommand command) {
        if (command.getTts() == null) {
            throw new TtsException("TtsDto details cannot be null");
        }
        if (command.getTts().getName() == null || command.getTts().getName().isBlank()) {
            throw new TtsException("TtsDto name cannot be null or empty");
        }
        apply(new TtsUpdatedEvent(command.getId(), command.getTts()));
        return this.tts;
    }

    @EventSourcingHandler
    public void on(TtsCreatedEvent event) {
        this.id = event.getId();
        this.tts = event.getTts();
    }

    @EventSourcingHandler
    public void on(TtsUpdatedEvent event) {
        this.id = event.getId();
        this.tts = event.getTts();
    }
} 