package org.hishab.agent.core.events.stt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;
import org.hishab.agent.core.model.stt.Stt;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Revision("1.0")
public class SttCreatedEvent {
    String id;
    Stt stt;
} 