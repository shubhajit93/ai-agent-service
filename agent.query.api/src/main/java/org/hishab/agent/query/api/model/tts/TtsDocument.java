package org.hishab.agent.query.api.model.tts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.core.model.tts.Voice;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "tts")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TtsDocument implements Serializable {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;  // Unique constraint on the provider field

    private TtsStatus status;  // ACTIVE or DELETED
    private List<Voice> voices;
    private List<String> models = new ArrayList<>();
    private double voiceTemperature;
    private Instant createdAt;
    private Instant modifiedAt;
}
