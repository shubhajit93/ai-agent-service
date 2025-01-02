package org.hishab.agent.core.model.agent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
//@Builder
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Agent implements Serializable {
    @JsonProperty("ai_agent_id")
    private String agentId;
    @JsonProperty("agent_name")
    private String agentName;
    @JsonProperty("agent_status")
    private String agentStatus;
    @JsonProperty("language_code")
    private String languageCode;
    private Llm llm;
    private AgentTts tts;
    private AgentStt stt;
    @JsonProperty("enable_user_interruptions")
    private boolean enableUserInterruptions;
    @JsonProperty("minimum_speech_duration_for_interruptions")
    private double minimumSpeechDurationForInterruptions;
    @JsonProperty("minimum_words_before_interruption")
    private int minimumWordsBeforeInterruption;
    @JsonProperty("wait_time_before_detecting_end_of_speech")
    private double waitTimeBeforeDetectingEndOfSpeech;
    @JsonProperty("ambient_sound")
    private String ambientSound;
    @JsonProperty("ambient_sound_volume")
    private int ambientSoundVolume;
    @JsonProperty("webhook_timeout")
    private String webhookUrl;
    @JsonProperty("end_call_after_silence_seconds")
    private int endCallAfterSilenceSeconds;
    @JsonProperty("max_call_duration_seconds")
    private int maxCallDurationSeconds;
    @JsonProperty("welcome_message")
    private String welcomeMessage;
    @JsonProperty("voicemail_detection_timeout_seconds")
    private int voicemailDetectionTimeoutSeconds;
    @JsonProperty("dynamic_data_configs")
    private List<DynamicDataConfig> dynamicDataConfigs;
    @JsonProperty("post_call_analysis")
    private List<PostCallAnalysis> postCallAnalysis;
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant createdAt;
    @JsonProperty("modified_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
    private Instant modifiedAt;
}
