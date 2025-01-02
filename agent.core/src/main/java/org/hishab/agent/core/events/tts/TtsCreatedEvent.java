package org.hishab.agent.core.events.tts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.serialization.Revision;
import org.hishab.agent.core.model.tts.Tts;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Revision("1.0")
public class TtsCreatedEvent {
    String id;
    Tts tts;
}
