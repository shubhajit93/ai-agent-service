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
import org.hishab.agent.cmd.api.commands.CreateSttCommand;
import org.hishab.agent.cmd.api.commands.UpdateSttCommand;
import org.hishab.agent.core.events.stt.SttCreatedEvent;
import org.hishab.agent.core.events.stt.SttUpdatedEvent;
import org.hishab.agent.core.exception.SttException;
import org.hishab.agent.core.model.stt.Stt;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Data
@NoArgsConstructor
@Aggregate(snapshotTriggerDefinition = "sttAggregateSnapshotTriggerDefinition")
@Slf4j
public class SttAggregate {

    @AggregateIdentifier
    private String id;
    private Stt stt;

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Stt handle(CreateSttCommand command) {
        if (command.getStt() == null) {
            throw new SttException("SttDto details cannot be null");
        }
        if (command.getStt().getName() == null || command.getStt().getName().isBlank()) {
            throw new SttException("SttDto name cannot be null or empty");
        }
        apply(new SttCreatedEvent(command.getId(), command.getStt()));
        return this.stt;
    }

    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    public Stt handle(UpdateSttCommand command) {
        if (command.getId() == null || command.getId().isBlank()) {
            throw new SttException("SttDto ID cannot be null or empty");
        }
        if (command.getStt() == null) {
            throw new SttException("SttDto details cannot be null");
        }
        apply(new SttUpdatedEvent(command.getId(), command.getStt()));
        return this.stt;
    }


    @EventSourcingHandler
    public void on(SttCreatedEvent event) {
        this.id = event.getId();
        this.stt = event.getStt();
    }

    @EventSourcingHandler
    public void on(SttUpdatedEvent event) {
        this.id = event.getId();
        this.stt = event.getStt();
    }
} 