package org.hishab.agent.core.model.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Llm {
    @JsonProperty("llm_id")
    private String llmId;
    @JsonProperty("llm_type")
    private String llmType;
    @JsonProperty("model_provider")
    private String modelProvider;
    @JsonProperty("model_name")
    private String modelName;
    @JsonProperty("model_temperature")
    private double modelTemperature;
    @JsonProperty("required_dynamic_data")
    private List<String> requiredDynamicData;
    @JsonProperty("system_prompt")
    private String systemPrompt;
    private List<Map<String, String>> tools;
}
