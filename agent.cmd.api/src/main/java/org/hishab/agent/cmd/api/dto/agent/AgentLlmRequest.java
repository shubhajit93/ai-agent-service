package org.hishab.agent.cmd.api.dto.agent;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AgentLlmRequest {
    private String llmId;
    private String llmType;
    private String modelProvider;
    private String modelName;
    private double modelTemperature;
    private List<String> requiredDynamicData;
    private String systemPrompt;
    private List<Map<String, String>> tools;
}
