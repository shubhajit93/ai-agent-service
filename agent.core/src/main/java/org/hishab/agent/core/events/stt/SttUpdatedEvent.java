package org.hishab.agent.core.events.stt;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hishab.agent.core.model.stt.Stt;

@Data
@AllArgsConstructor
public class SttUpdatedEvent {
    private final String id;
    private final Stt stt;
} 