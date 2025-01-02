package org.hishab.agent.cmd.api.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.hishab.agent.core.model.stt.Stt;

@Builder
@Data
public class UpdateSttCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final Stt stt;
} 