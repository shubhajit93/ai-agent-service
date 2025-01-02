package org.hishab.agent.cmd.api.dto.llm;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LlmResponseWrapper {
    private String llmId;
    private String llmType;
    private String modelProvider;
    private String modelName;
    private double modelTemperature;
    private Object requiredDynamicData; // Use appropriate type if known
    private String systemPrompt;
    private Object tools;
}
