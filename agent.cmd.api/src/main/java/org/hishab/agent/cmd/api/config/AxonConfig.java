package org.hishab.agent.cmd.api.config;

import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.SnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public SnapshotTriggerDefinition agentAggregateSnapshotTriggerDefinition(Snapshotter snapshotter, @Value("${axon.aggregate.agentDto.snapshot-threshold:250}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }

    @Bean
    public SnapshotTriggerDefinition sttAggregateSnapshotTriggerDefinition(Snapshotter snapshotter, @Value("${axon.aggregate.sttDto.snapshot-threshold:250}") int threshold) {
        return new EventCountSnapshotTriggerDefinition(snapshotter, threshold);
    }
}