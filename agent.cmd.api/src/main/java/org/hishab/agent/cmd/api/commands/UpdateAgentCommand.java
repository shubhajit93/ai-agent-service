package org.hishab.agent.cmd.api.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import org.hishab.agent.core.model.agent.Agent;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAgentCommand {
    @TargetAggregateIdentifier
    private String id;
    private Agent agent;
}