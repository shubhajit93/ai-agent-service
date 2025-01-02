package org.hishab.agent.cmd.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.hishab.agent.cmd.api.commands.CreateAgentCommand;
import org.hishab.agent.cmd.api.commands.DeleteAgentCommand;
import org.hishab.agent.cmd.api.commands.UpdateAgentCommand;
import org.hishab.agent.cmd.api.dto.agent.AgentPostCallAnalysisRequest;
import org.hishab.agent.cmd.api.dto.agent.AgentRequest;
import org.hishab.agent.cmd.api.dto.agent.DynamicDataConfig;
import org.hishab.agent.cmd.api.dto.agent.ResponseData;
import org.hishab.agent.cmd.api.dto.llm.LlmResponseWrapper;
import org.hishab.agent.cmd.api.service.AgentService;
import org.hishab.agent.cmd.api.validation.SttValidator;
import org.hishab.agent.cmd.api.validation.TtsValidator;
import org.hishab.agent.cmd.api.validation.ValidationType;
import org.hishab.agent.core.dto.ApiResponse;
import org.hishab.agent.core.dto.ErrorResponse;
import org.hishab.agent.core.enums.ResponseStatus;
import org.hishab.agent.core.exception.AgentException;
import org.hishab.agent.core.model.agent.Agent;
import org.hishab.agent.core.model.agent.AgentStt;
import org.hishab.agent.core.model.agent.AgentTts;
import org.hishab.agent.core.model.agent.Llm;
import org.hishab.agent.core.utils.StringRelatedUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AgentServiceImpl implements AgentService {
    private final CommandGateway commandGateway;
    private final SttValidator sttValidator;
    private final TtsValidator ttsValidator;
    private final LlmServiceImpl llmService;

    public AgentServiceImpl(CommandGateway commandGateway, SttValidator sttValidator, TtsValidator ttsValidator, LlmServiceImpl llmService) {
        this.commandGateway = commandGateway;
        this.sttValidator = sttValidator;
        this.ttsValidator = ttsValidator;
        this.llmService = llmService;
    }

    @Override
    public CompletableFuture<ApiResponse<Agent>> createAgent(AgentRequest agentRequest) throws AgentException {
        sttValidator.isValidSttInformation(agentRequest.getStt(), ValidationType.STTCREATION);
        ttsValidator.isValidTtsInformation(agentRequest.getTts(), ValidationType.TTSCREATION);
        CreateAgentCommand createAgentCommand = prepareCreateAgentCommand(agentRequest);
        return commandGateway.send(createAgentCommand)
                .thenApply(result -> {
                    Agent agentResponse = (Agent) result;
                    return new ApiResponse<>(agentResponse, "Agent Created Successfully", ResponseStatus.SUCCESS.value, null);
                })
                .whenComplete((agentRegisteredResponse, throwable) -> log.info("{}", agentRegisteredResponse.getData()))
                .exceptionally(ex -> {
                    log.error("Failed to register agent. reason is {}", ex.getMessage());
                    try {
                        throw new AgentException(ErrorResponse.VALIDATION_ERROR, ex.getMessage(), null, null);
                    } catch (AgentException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<ApiResponse<Agent>> updateAgent(String agentId, AgentRequest agentRequest) throws AgentException {
        if (StringRelatedUtils.isNullOrEmpty(agentId)) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent Id is missing", null, null);
        }
        sttValidator.isValidSttInformation(agentRequest.getStt(), ValidationType.STTUPDATE);
        ttsValidator.isValidTtsInformation(agentRequest.getTts(), ValidationType.TTSUPDATE);

        UpdateAgentCommand updateAgentCommand = prepareUpdateAgentCommand(agentId, agentRequest);


        return commandGateway.send(updateAgentCommand)
                .thenApply(result -> {
                    Agent agentResponse = (Agent) result;
                    return new ApiResponse<>(agentResponse, "Agent Updated Successfully", ResponseStatus.SUCCESS.value, null);
                })
                .exceptionally(ex -> {
                    log.error("Failed to update agent. agentId {}, reason is {}", agentRequest.getAgentId(), ex.getMessage());
                    try {
                        throw new AgentException(ErrorResponse.VALIDATION_ERROR, ex.getMessage(), null, null);
                    } catch (AgentException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public CompletableFuture<ApiResponse<Object>> deactivateAgent(String agentId) throws AgentException {
        if (StringRelatedUtils.isNullOrEmpty(agentId)) {
            throw new AgentException(ErrorResponse.VALIDATION_ERROR, "Agent Id is missing", null, null);
        }
        return commandGateway.send(new DeleteAgentCommand(UUID.randomUUID().toString(), Agent.builder().agentId(agentId).build()))
                .thenApply(result -> new ApiResponse<>(null, "Agent Deactivated Successfully", ResponseStatus.SUCCESS.value, null))
                .exceptionally(ex -> {
                    log.error("Failed to deactivate agent. agentId {}, reason is {}", agentId, ex.getMessage());
                    return new ApiResponse<>(null, ex.getMessage(), ResponseStatus.FAILURE.value, null);
                });
    }

    private org.hishab.agent.core.model.agent.DynamicDataConfig convertDynamicDataToCoreModel(
            DynamicDataConfig dtoConfig) {
        return org.hishab.agent.core.model.agent.DynamicDataConfig.builder()
                .url(dtoConfig.getUrl())
                .method(dtoConfig.getMethod())
                .timeout(dtoConfig.getTimeout())
                .headers(dtoConfig.getHeaders())
                .body(dtoConfig.getBody())
                .query(dtoConfig.getQuery())
                .cache(dtoConfig.isCache())
                .responseData(dtoConfig.getResponseData().stream()
                        .map(this::convertResponseDataToCoreModel)
                        .collect(Collectors.toList()))
                .build();
    }

    private org.hishab.agent.core.model.agent.PostCallAnalysis convertPostCallAnalysisToCoreModel(
            AgentPostCallAnalysisRequest postCallAnalysisRequest
    ) {
        return org.hishab.agent.core.model.agent.PostCallAnalysis.builder()
                .type(postCallAnalysisRequest.getType())
                .name(postCallAnalysisRequest.getName())
                .description(postCallAnalysisRequest.getDescription())
                .systemPrompt(postCallAnalysisRequest.getSystemPrompt())
                .examples(postCallAnalysisRequest.getExamples())
                .choices(postCallAnalysisRequest.getChoices())
                .build();
    }

    private org.hishab.agent.core.model.agent.ResponseData convertResponseDataToCoreModel(
            ResponseData dtoResponseData) {
        return org.hishab.agent.core.model.agent.ResponseData.builder()
                .data(dtoResponseData.getData())
                .context(dtoResponseData.getContext())
                .name(dtoResponseData.getName())
                .build();
    }

    private CreateAgentCommand prepareCreateAgentCommand(AgentRequest agentRequest) throws AgentException {
        String traceId = UUID.randomUUID().toString();
        if (agentRequest.getLlm().getLlmId() == null || agentRequest.getLlm().getLlmId().isBlank()) {
            LlmResponseWrapper llmResponseWrapper = llmService.createLlm(agentRequest.getLlm()).block();
            if (llmResponseWrapper == null) {
                throw new AgentException(ErrorResponse.INTERNAL_ERROR, "Failed to create LLM", null, null);
            }
            agentRequest.getLlm().setLlmId(llmResponseWrapper.getLlmId());
        }
        Agent agent = Agent.builder()
                .agentId(traceId)
                .agentName(agentRequest.getAgentName())
                .agentStatus(agentRequest.getAgentStatus())
                .languageCode(agentRequest.getLanguageCode())
                .llm(Llm.builder()
                        .llmId(agentRequest.getLlm().getLlmId())
                        .llmType(agentRequest.getLlm().getLlmType())
                        .modelProvider(agentRequest.getLlm().getModelProvider())
                        .modelName(agentRequest.getLlm().getModelName())
                        .modelTemperature(agentRequest.getLlm().getModelTemperature())
                        .requiredDynamicData(agentRequest.getLlm().getRequiredDynamicData())
                        .systemPrompt(agentRequest.getLlm().getSystemPrompt())
                        .tools(agentRequest.getLlm().getTools())
                        .build())
                .stt(AgentStt.builder()
                        .provider(agentRequest.getStt().getProvider())
                        .model(agentRequest.getStt().getModel())
                        .build())
                .tts(AgentTts.builder()
                        .modelName(agentRequest.getTts().getModelName())
                        .provider(agentRequest.getTts().getProvider())
                        .voiceName(agentRequest.getTts().getVoiceName())
                        .voiceId(agentRequest.getTts().getVoiceId())
                        .voiceTemperature(agentRequest.getTts().getVoiceTemperature())
                        .build())
                .enableUserInterruptions(agentRequest.isEnableUserInterruptions())
                .minimumSpeechDurationForInterruptions(agentRequest.getMinimumSpeechDurationForInterruptions())
                .minimumWordsBeforeInterruption(agentRequest.getMinimumWordsBeforeInterruption())
                .waitTimeBeforeDetectingEndOfSpeech(agentRequest.getWaitTimeBeforeDetectingEndOfSpeech())
                .ambientSound(agentRequest.getAmbientSound())
                .ambientSoundVolume(agentRequest.getAmbientSoundVolume())
                .enableUserInterruptions(agentRequest.isEnableUserInterruptions())
                .webhookUrl(agentRequest.getWebhookUrl())
                .endCallAfterSilenceSeconds(agentRequest.getEndCallAfterSilenceSeconds())
                .maxCallDurationSeconds(agentRequest.getMaxCallDurationSeconds())
                .welcomeMessage(agentRequest.getWelcomeMessage())
                .voicemailDetectionTimeoutSeconds(agentRequest.getVoicemailDetectionTimeoutSeconds())
                .dynamicDataConfigs(agentRequest.getDynamicDataConfigs() != null ?
                        agentRequest.getDynamicDataConfigs().stream()
                                .map(this::convertDynamicDataToCoreModel)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .postCallAnalysis(agentRequest.getPostCallAnalysis() != null ?
                        agentRequest.getPostCallAnalysis().stream()
                                .map(this::convertPostCallAnalysisToCoreModel)
                                .collect(Collectors.toList()) : Collections.emptyList())
                .createdAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                .modifiedAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant())
                .build();
        return CreateAgentCommand.builder()
                .id(traceId)
                .agent(agent)
                .build();
    }

    private UpdateAgentCommand prepareUpdateAgentCommand(String agentId, AgentRequest agentRequest) {
        Agent.AgentBuilder<?, ?> agentBuilder = Agent.builder();
        agentBuilder.agentId(agentId);

        if (agentRequest.getAgentName() != null) {
            agentBuilder.agentName(agentRequest.getAgentName());
        }

        if (agentRequest.getAgentStatus() != null) {
            agentBuilder.agentStatus(agentRequest.getAgentStatus());
        }
        if (agentRequest.getLanguageCode() != null) {
            agentBuilder.languageCode(agentRequest.getLanguageCode());
        }
        if (agentRequest.getLlm() != null) {
            Llm.LlmBuilder llmBuilder = Llm.builder();
            if (agentRequest.getLlm().getLlmType() != null) {
                llmBuilder.llmType(agentRequest.getLlm().getLlmType());
            }
            if (agentRequest.getLlm().getModelProvider() != null) {
                llmBuilder.modelProvider(agentRequest.getLlm().getModelProvider());
            }
            if (agentRequest.getLlm().getModelName() != null) {
                llmBuilder.modelName(agentRequest.getLlm().getModelName());
            }
            llmBuilder.modelTemperature(agentRequest.getLlm().getModelTemperature());
            llmBuilder.requiredDynamicData(agentRequest.getLlm().getRequiredDynamicData());
            llmBuilder.systemPrompt(agentRequest.getLlm().getSystemPrompt());
            llmBuilder.tools(agentRequest.getLlm().getTools());
            agentBuilder.llm(llmBuilder.build());
        }

        if (agentRequest.getStt() != null) {
            AgentStt.AgentSttBuilder sttBuilder = AgentStt.builder();
            if (agentRequest.getStt().getProvider() != null) {
                sttBuilder.provider(agentRequest.getStt().getProvider());
            }
            if (agentRequest.getStt().getModel() != null) {
                sttBuilder.model(agentRequest.getStt().getModel());
            }
            agentBuilder.stt(sttBuilder.build());
        }

        if (agentRequest.getTts() != null) {
            AgentTts.AgentTtsBuilder ttsBuilder = AgentTts.builder();
            if (agentRequest.getTts().getModelName() != null) {
                ttsBuilder.modelName(agentRequest.getTts().getModelName());
            }
            if (agentRequest.getTts().getProvider() != null) {
                ttsBuilder.provider(agentRequest.getTts().getProvider());
            }
            if (agentRequest.getTts().getVoiceName() != null) {
                ttsBuilder.voiceName(agentRequest.getTts().getVoiceName());
            }
            if (agentRequest.getTts().getVoiceId() != null) {
                ttsBuilder.voiceId(agentRequest.getTts().getVoiceId());
            }
            ttsBuilder.voiceTemperature(agentRequest.getTts().getVoiceTemperature());
            agentBuilder.tts(ttsBuilder.build());
        }
        agentBuilder.enableUserInterruptions(agentRequest.isEnableUserInterruptions());
        agentBuilder.minimumSpeechDurationForInterruptions(agentRequest.getMinimumSpeechDurationForInterruptions());
        agentBuilder.minimumWordsBeforeInterruption(agentRequest.getMinimumWordsBeforeInterruption());
        agentBuilder.waitTimeBeforeDetectingEndOfSpeech(agentRequest.getWaitTimeBeforeDetectingEndOfSpeech());

        if (agentRequest.getAmbientSound() != null) {
            agentBuilder.ambientSound(agentRequest.getAmbientSound());
        }
        agentBuilder.ambientSoundVolume(agentRequest.getAmbientSoundVolume());

        if (agentRequest.getWebhookUrl() != null) {
            agentBuilder.webhookUrl(agentRequest.getWebhookUrl());
        }

        agentBuilder.endCallAfterSilenceSeconds(agentRequest.getEndCallAfterSilenceSeconds());
        agentBuilder.maxCallDurationSeconds(agentRequest.getMaxCallDurationSeconds());

        if (agentRequest.getWelcomeMessage() != null) {
            agentBuilder.welcomeMessage(agentRequest.getWelcomeMessage());
        }

        agentBuilder.voicemailDetectionTimeoutSeconds(agentRequest.getVoicemailDetectionTimeoutSeconds());

        if (agentRequest.getDynamicDataConfigs() != null) {
            agentBuilder.dynamicDataConfigs(agentRequest.getDynamicDataConfigs().stream()
                    .map(this::convertDynamicDataToCoreModel)
                    .collect(Collectors.toList()));
        }

        agentBuilder.modifiedAt(OffsetDateTime.now(ZoneId.of("UTC")).toInstant());

        String traceId = UUID.randomUUID().toString();
        return UpdateAgentCommand.builder()
                .id(traceId)
                .agent(agentBuilder.build())
                .build();
    }
}
