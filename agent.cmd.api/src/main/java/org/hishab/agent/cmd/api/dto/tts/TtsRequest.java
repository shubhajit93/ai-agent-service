package org.hishab.agent.cmd.api.dto.tts;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hishab.agent.core.enums.TtsStatus;
import org.hishab.agent.core.model.tts.Voice;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TtsRequest {
    private String name;
    private TtsStatus status;
    private List<String> models;
    private List<Voice> voices;
    private double voiceTemperature;
}
