package org.hishab.agent.cmd.api.dto.agent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//make all fields snake case
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AgentRequest {
    @JsonProperty("ai_agent_id")
    private String agentId;
    @NotBlank(message = "AgentDto name cannot be blank")
    @Size(min = 2, max = 100, message = "AgentDto name must be between 2 and 100 characters")
    private String agentName;
    private String languageCode;
    private AgentLlmRequest llm;
    @NotNull(message = "AgentDto status cannot be null")
    @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "AgentDto status must be ACTIVE, INACTIVE, or SUSPENDED")
    private String agentStatus = "ACTIVE";

    @Schema(description = "Text-to-Speech configuration")
    private AgentTtsRequest tts;

    @Schema(description = "Speech-to-Text configuration")
    private AgentSttRequest stt;

    private String knowledgeBaseId;
    private boolean enableUserInterruptions;
    private double minimumSpeechDurationForInterruptions;
    private int minimumWordsBeforeInterruption;
    private double waitTimeBeforeDetectingEndOfSpeech;
    private String ambientSound;
    private int ambientSoundVolume;
    private String webhookUrl;
    private int endCallAfterSilenceSeconds;
    private int maxCallDurationSeconds;
    private String welcomeMessage;
    private int voicemailDetectionTimeoutSeconds;

    //    @Schema(description = "Dynamic data configuration")
    private List<DynamicDataConfig> dynamicDataConfigs;
    private List<AgentPostCallAnalysisRequest> postCallAnalysis;
}
