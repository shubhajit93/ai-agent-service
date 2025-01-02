package org.hishab.agent.core.model.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentTts {
    private String provider;
    @JsonProperty("voice_id")
    private String voiceId;
    @JsonProperty("voice_name")
    private String voiceName;
    @JsonProperty("model_name")
    private String modelName;
    @JsonProperty("model_temperature")
    private double voiceTemperature;
}
