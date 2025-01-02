package org.hishab.agent.query.api.handlers.events;

import org.axonframework.eventhandling.EventHandler;
import org.hishab.agent.core.enums.AgentStatus;
import org.hishab.agent.core.events.agent.AgentCreatedEvent;
import org.hishab.agent.core.events.agent.AgentDeletedEvent;
import org.hishab.agent.core.events.agent.AgentUpdatedEvent;
import org.hishab.agent.core.model.agent.AgentStt;
import org.hishab.agent.core.model.agent.AgentTts;
import org.hishab.agent.core.model.agent.Llm;
import org.hishab.agent.query.api.model.agent.AgentDocument;
import org.hishab.agent.query.api.repositories.AgentRepository;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AgentEventHandler {
    private final AgentRepository agentRepository;
    private final ThreadPoolTaskExecutor virtualThreadExecutor;

    public AgentEventHandler(AgentRepository agentRepository, ThreadPoolTaskExecutor virtualThreadExecutor) {
        this.agentRepository = agentRepository;
        this.virtualThreadExecutor = virtualThreadExecutor;
    }

    @EventHandler
    public void on(AgentCreatedEvent event) {
        virtualThreadExecutor.execute(() -> agentRepository.save(AgentDocument
                .builder()
                .agentId(event.getId())
                .agentName(event.getAgent().getAgentName())
                .agentStatus(event.getAgent().getAgentStatus())
                .languageCode(event.getAgent().getLanguageCode())
                .llm(Llm.builder()
                        .llmId(event.getAgent().getLlm().getLlmId())
                        .llmType(event.getAgent().getLlm().getLlmType())
                        .modelProvider(event.getAgent().getLlm().getModelProvider())
                        .modelName(event.getAgent().getLlm().getModelName())
                        .modelTemperature(event.getAgent().getLlm().getModelTemperature())
                        .requiredDynamicData(event.getAgent().getLlm().getRequiredDynamicData())
                        .systemPrompt(event.getAgent().getLlm().getSystemPrompt())
                        .tools(event.getAgent().getLlm().getTools())
                        .build())
                .stt(AgentStt
                        .builder()
                        .provider(event.getAgent().getStt().getProvider())
                        .model(event.getAgent().getStt().getModel())
                        .build())
                .tts(AgentTts.builder()
                        .modelName(event.getAgent().getTts().getModelName())
                        .provider(event.getAgent().getTts().getProvider())
                        .voiceName(event.getAgent().getTts().getVoiceName())
                        .voiceId(event.getAgent().getTts().getVoiceId())
                        .voiceTemperature(event.getAgent().getTts().getVoiceTemperature())
                        .build())
                .enableUserInterruptions(event.getAgent().isEnableUserInterruptions())
                .minimumSpeechDurationForInterruptions(event.getAgent().getMinimumSpeechDurationForInterruptions())
                .minimumWordsBeforeInterruption(event.getAgent().getMinimumWordsBeforeInterruption())
                .waitTimeBeforeDetectingEndOfSpeech(event.getAgent().getWaitTimeBeforeDetectingEndOfSpeech())
                .ambientSound(event.getAgent().getAmbientSound())
                .ambientSoundVolume(event.getAgent().getAmbientSoundVolume())
                .enableUserInterruptions(event.getAgent().isEnableUserInterruptions())
                .waitTimeBeforeDetectingEndOfSpeech(event.getAgent().getWaitTimeBeforeDetectingEndOfSpeech())
                .webhookUrl(event.getAgent().getWebhookUrl())
                .endCallAfterSilenceSeconds(event.getAgent().getEndCallAfterSilenceSeconds())
                .maxCallDurationSeconds(event.getAgent().getMaxCallDurationSeconds())
                .welcomeMessage(event.getAgent().getWelcomeMessage())
                .voicemailDetectionTimeoutSeconds(event.getAgent().getVoicemailDetectionTimeoutSeconds())
                .dynamicDataConfigs(event.getAgent().getDynamicDataConfigs())
                .postCallAnalysis(event.getAgent().getPostCallAnalysis())
                .createdAt(event.getAgent().getCreatedAt())
                .modifiedAt(event.getAgent().getModifiedAt())
                .build())
        );
    }

    @EventHandler
    public void on(AgentUpdatedEvent event) {
        virtualThreadExecutor.execute(() -> {
            AgentDocument agentFromDb = agentRepository.findByAgentId(event.getAgent().getAgentId()).orElse(null);
            if (agentFromDb != null) {

                if (event.getAgent().getAgentName() != null) {
                    agentFromDb.setAgentName(event.getAgent().getAgentName());
                }

                if (event.getAgent().getAgentStatus() != null) {
                    agentFromDb.setAgentStatus(event.getAgent().getAgentStatus());
                }
                if (event.getAgent().getLanguageCode() != null) {
                    agentFromDb.setLanguageCode(event.getAgent().getLanguageCode());
                }
                if (event.getAgent().getLlm() != null) {
                    if (event.getAgent().getLlm().getLlmType() != null) {
                        agentFromDb.getLlm().setLlmType(event.getAgent().getLlm().getLlmType());
                    }
                    if (event.getAgent().getLlm().getModelProvider() != null) {
                        agentFromDb.getLlm().setModelProvider(event.getAgent().getLlm().getModelProvider());
                    }
                    if (event.getAgent().getLlm().getModelName() != null) {
                        agentFromDb.getLlm().setModelName(event.getAgent().getLlm().getModelName());
                    }
                    if (event.getAgent().getLlm().getModelTemperature() != agentFromDb.getLlm().getModelTemperature()) {
                        agentFromDb.getLlm().setModelTemperature(event.getAgent().getLlm().getModelTemperature());
                    }
                    if (event.getAgent().getLlm().getRequiredDynamicData() != null) {
                        agentFromDb.getLlm().setRequiredDynamicData(event.getAgent().getLlm().getRequiredDynamicData());
                    }
                    if (event.getAgent().getLlm().getSystemPrompt() != null) {
                        agentFromDb.getLlm().setSystemPrompt(event.getAgent().getLlm().getSystemPrompt());
                    }
                    if (event.getAgent().getLlm().getTools() != null) {
                        agentFromDb.getLlm().setTools(event.getAgent().getLlm().getTools());
                    }
                }
                if (event.getAgent().getStt() != null) {
                    if (event.getAgent().getStt().getProvider() != null) {
                        agentFromDb.getStt().setProvider(event.getAgent().getStt().getProvider());
                    }
                    if (event.getAgent().getStt().getModel() != null) {
                        agentFromDb.getStt().setModel(event.getAgent().getStt().getModel());
                    }
                }

                if (event.getAgent().getTts() != null) {
                    if (event.getAgent().getTts().getModelName() != null) {
                        agentFromDb.getTts().setModelName(event.getAgent().getTts().getModelName());
                    }
                    if (event.getAgent().getTts().getProvider() != null) {
                        agentFromDb.getTts().setProvider(event.getAgent().getTts().getProvider());
                    }
                    if (event.getAgent().getTts().getVoiceName() != null) {
                        agentFromDb.getTts().setVoiceName(event.getAgent().getTts().getVoiceName());
                    }
                    if (event.getAgent().getTts().getVoiceId() != null) {
                        agentFromDb.getTts().setVoiceId(event.getAgent().getTts().getVoiceId());
                    }
                    if (event.getAgent().getTts().getVoiceTemperature() != agentFromDb.getTts().getVoiceTemperature()) {
                        agentFromDb.getTts().setVoiceTemperature(event.getAgent().getTts().getVoiceTemperature());
                    }
                }
                if (event.getAgent().isEnableUserInterruptions() != agentFromDb.isEnableUserInterruptions()) {
                    agentFromDb.setEnableUserInterruptions(event.getAgent().isEnableUserInterruptions());
                }
                if (event.getAgent().getMinimumSpeechDurationForInterruptions() != agentFromDb.getMinimumSpeechDurationForInterruptions()) {
                    agentFromDb.setMinimumSpeechDurationForInterruptions(event.getAgent().getMinimumSpeechDurationForInterruptions());
                }
                if (event.getAgent().getMinimumWordsBeforeInterruption() != agentFromDb.getMinimumWordsBeforeInterruption()) {
                    agentFromDb.setMinimumWordsBeforeInterruption(event.getAgent().getMinimumWordsBeforeInterruption());
                }
                if (event.getAgent().getWaitTimeBeforeDetectingEndOfSpeech() != agentFromDb.getWaitTimeBeforeDetectingEndOfSpeech()) {
                    agentFromDb.setWaitTimeBeforeDetectingEndOfSpeech(event.getAgent().getWaitTimeBeforeDetectingEndOfSpeech());
                }
                if (event.getAgent().getAmbientSound() != null) {
                    agentFromDb.setAmbientSound(event.getAgent().getAmbientSound());
                }
                if (event.getAgent().getAmbientSoundVolume() != agentFromDb.getAmbientSoundVolume()) {
                    agentFromDb.setAmbientSoundVolume(event.getAgent().getAmbientSoundVolume());
                }
                if (event.getAgent().getWebhookUrl() != null) {
                    agentFromDb.setWebhookUrl(event.getAgent().getWebhookUrl());
                }
                if (event.getAgent().getEndCallAfterSilenceSeconds() != agentFromDb.getEndCallAfterSilenceSeconds()) {
                    agentFromDb.setEndCallAfterSilenceSeconds(event.getAgent().getEndCallAfterSilenceSeconds());
                }
                if (event.getAgent().getMaxCallDurationSeconds() != agentFromDb.getMaxCallDurationSeconds()) {
                    agentFromDb.setMaxCallDurationSeconds(event.getAgent().getMaxCallDurationSeconds());
                }
                if (event.getAgent().getWelcomeMessage() != null) {
                    agentFromDb.setWelcomeMessage(event.getAgent().getWelcomeMessage());
                }
                if (event.getAgent().getVoicemailDetectionTimeoutSeconds() != agentFromDb.getVoicemailDetectionTimeoutSeconds()) {
                    agentFromDb.setVoicemailDetectionTimeoutSeconds(event.getAgent().getVoicemailDetectionTimeoutSeconds());
                }
                if (event.getAgent().getDynamicDataConfigs() != null) {
                    agentFromDb.setDynamicDataConfigs(event.getAgent().getDynamicDataConfigs());
                }
                if (event.getAgent().getPostCallAnalysis() != null) {
                    agentFromDb.setPostCallAnalysis(event.getAgent().getPostCallAnalysis());
                }
                agentFromDb.setModifiedAt(Instant.now());
                agentRepository.save(agentFromDb);
            } else {
                throw new RuntimeException("Agent not found");
            }
        });
    }

    @EventHandler
    public void on(AgentDeletedEvent event) {
        virtualThreadExecutor.execute(() -> {
            AgentDocument agentFromDb = agentRepository.findByAgentId(event.getAgent().getAgentId()).orElse(null);
            if (agentFromDb != null) {
                agentFromDb.setAgentStatus(AgentStatus.INACTIVE.name());
                agentRepository.save(agentFromDb);
            } else {
                throw new RuntimeException("Agent not found");
            }
        });
    }

}